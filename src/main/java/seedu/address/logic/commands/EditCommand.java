package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class EditCommand extends Command {
    
    public static final String[] COMMAND_WORD = {
            "edit",
            "postpone"
    };
    
    public static final int INVALID_VALUE_LENGTH = 0;
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;
    public static final String NOTHING_CHANGED = "Nothing Changed!" ;

    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD
            + ": Edits the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer) NAME | d/DESCRIPTION | e/TAG...\n"
            + "Example: " + DEFAULT_COMMAND_WORD + " 1 d/download How I Met Your Mother season 1" ;
    public static final String MESSAGE_EDIT_SUCCESS = "Edit saved!";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";
    
    private static final String START_DATE = "startDate" ;
    private static final String END_DATE = "endDate" ;

    private final int targetIndex;
    private final String name;
    private final String description;
    private boolean doneStatus;
    private final Set<Tag> tagSet;
    private final Map<String, LocalDateTime> dateMap ;
    
    private boolean hasChanged = false ;
    
    public EditCommand(int targetIndex, String name, String description, Set<String> tags, LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.name = name;
        this.description = description;
        this.tagSet = Sets.newHashSet() ;
        this.dateMap = Maps.newHashMap() ;
        
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        if (startDate != null) {
            dateMap.put(START_DATE, startDate) ;
        }
        
        if (endDate != null) {
            dateMap.put(END_DATE, endDate) ;
        }

    }
    
    public EditCommand(int targetIndex, String name, String description, Set<String> tags) throws IllegalValueException {
        this (targetIndex, name, description, tags, null, null) ;
    }
    
    public EditCommand(int targetIndex, String name, String description, Set<String> tags, LocalDateTime endDate) throws IllegalValueException {
        this (targetIndex, name, description, tags, null, endDate) ;
    }
    
    @Override
    public CommandResult execute() {
        String newName, newDescription;
        UniqueTagList newTagSet;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex || targetIndex < 1) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);  
        doneStatus = taskToEdit.getDoneStatus();
       
        if(isValidString(name)) {
            newName = name;
            hasChanged = true ;
        }else{
            newName = taskToEdit.getName();
        }
        if(isValidString(description)) {
            newDescription = description;
            hasChanged = true ;
        }else{
            newDescription = taskToEdit.getDescription();
        }
        
        newTagSet = new UniqueTagList(editOrDeleteTags(taskToEdit.getTags(), tagSet)) ;
        
        determineDateTimeOfNewTask (taskToEdit) ;
        
        if(!hasChanged){
            return new CommandResult(NOTHING_CHANGED);
        }
       
        Task newTask = createNewTask (newName, newDescription, newTagSet, dateMap.get(START_DATE), dateMap.get(END_DATE));

    	model.recordTaskForce();
        try {

//            model.updateTask(taskToEdit, newTask);
        	model.addTask(newTask);
        	model.deleteTask(taskToEdit);
            
            return new CommandResult(String.format(MESSAGE_EDIT_SUCCESS, newTask));

        } catch (TaskNotFoundException pnfe) {
            return new CommandResult("The target task cannot be missing");
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
       
    }
    
    private boolean isValidString (String s) {
        return s.length() > INVALID_VALUE_LENGTH ;
    }
    
    private Task createNewTask (String name, String description, UniqueTagList tag, LocalDateTime startTime, LocalDateTime endTime) {
        
        int id = model.getNextTaskId() ;
        
        if (startTime != null && endTime != null) {
            return new Event (id, name, description, startTime, endTime, tag, doneStatus) ;
        
        } 
        
        if (endTime != null && startTime == null) {
            return new Deadline (id, name, description, endTime, tag, doneStatus) ;
        
        } 
        
        return new Task (id, name, description, tag, doneStatus) ;

    }
    
    private void determineDateTimeOfNewTask (ReadOnlyTask taskToEdit) {
        if (taskToEdit instanceof Event) {

            if (!dateMap.containsKey(START_DATE)) {
                dateMap.put(START_DATE, ( (Event) taskToEdit).getStartDate()) ;
            } else {
                hasChanged = true ;
            }

            if (!dateMap.containsKey(END_DATE)) {
                dateMap.put(END_DATE, ( (Event) taskToEdit).getEndDate()) ;
            } else {
                hasChanged = true ;
            }

        }

        if (taskToEdit instanceof Deadline) {

            if (!dateMap.containsKey(END_DATE)) {
                dateMap.put(END_DATE, ( (Deadline) taskToEdit).getEndDate()) ;
            } else {
                hasChanged = true ;
            }
        }
    }
    
    private Set<Tag> editOrDeleteTags(UniqueTagList currentTags, Set<Tag> tagSet) {
        
        Set<Tag> newTaskTags = Sets.newHashSet(currentTags) ;
        
        for (Tag tag : tagSet) {
            if (!currentTags.contains(tag)) {
                newTaskTags.add(tag) ;
                hasChanged = true ;
                
            } else {
                newTaskTags.remove(tag) ;
                hasChanged = true ;
            }
            
        }
        
        return newTaskTags ;
    }

}
