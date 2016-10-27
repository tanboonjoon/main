package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javafx.util.Pair;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Block;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/*
 * Edits a existing task in all ways possible
 * @@author A0111277M
 */
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
    public static final String MESSAGE_BLOCK_CANNOT_REMOVE_DATE = "The target is a block, and dates cannot be removed.";
    public static final String MESSAGE_ST_WITHOUT_ET = "You input a start date without an end date!";
    public static final String MESSAGE_CANNOT_HAVE_ST_ONLY = "You can't only have the start time in a task!";
    
    private static final String START_DATE = "startDate" ;
    private static final String END_DATE = "endDate" ;
    private List<ReadOnlyTask> tasksAdded = Lists.newLinkedList();
    private List<ReadOnlyTask> tasksDeleted = Lists.newLinkedList();
    private final int targetIndex;
    private final String name;
    private final String description;
    private boolean doneStatus;
    private final Set<String> tagNameSet;
    private final Map<String, LocalDateTime> dateMap ;
    
    private boolean hasChanged = false ;
    
    public EditCommand(int targetIndex, String name, String description, Set<String> tags, LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.name = name;
        this.description = description;
        this.tagNameSet = tags ;
        this.dateMap = Maps.newHashMap() ;
        
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
        String newName;
        String newDescription;
        UniqueTagList newTagSet;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        Task newTask;
        
        /* determine target task to delete based on lastShownList */
        if (lastShownList.size() < targetIndex || targetIndex < 1) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);  
    
        newName = checkUpdate(taskToEdit.getName(), name);
        
        determineDateTimeOfNewTask(taskToEdit);
        
        if (taskToEdit instanceof Block) {
        	
        	if (dateMap.get(START_DATE) == null || dateMap.get(END_DATE) == null) {
        		return new CommandResult(MESSAGE_BLOCK_CANNOT_REMOVE_DATE);
        	}
        	
        	newTask = new Block(taskToEdit.getTaskId(), newName, dateMap.get(START_DATE), dateMap.get(END_DATE));
        	
        } else {
        	
        	if (dateMap.get(START_DATE) != null && dateMap.get(END_DATE) == null) {
        		return new CommandResult(MESSAGE_CANNOT_HAVE_ST_ONLY);
        	}

	        
	        newDescription = checkUpdate(taskToEdit.getDescription(), description);
	        
	        doneStatus = taskToEdit.getDoneStatus();
	        
	        try {
	            newTagSet = new UniqueTagList(editOrDeleteTags(taskToEdit.getTags(), tagNameSet)) ;
	        
	        } catch (IllegalValueException e) {
	            return new CommandResult(e.getMessage());
	        }
        
        	newTask = createNewTask (newName, newDescription, newTagSet, dateMap.get(START_DATE), dateMap.get(END_DATE));
        }
        

        
        if(!hasChanged){
            return new CommandResult(NOTHING_CHANGED);
        }

        try {
            model.addTask(newTask);
            tasksAdded.add(newTask);

            model.deleteTask(taskToEdit);
            tasksDeleted.add(taskToEdit);

            return new CommandResult(String.format(MESSAGE_EDIT_SUCCESS, newTask), true);

        } catch (TaskNotFoundException pnfe) {
            return new CommandResult("The target task cannot be missing");
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
       
    }
    
    private boolean isValidString (String s) {
        return s.length() > INVALID_VALUE_LENGTH ;
    }
    
    private String checkUpdate(String origin, String changed) {
    	if (isValidString(changed)) {
    		hasChanged = true;
    		return changed;
    	} else {
    		return origin;
    	}
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
    
    /*
     * Populates the dates map with existing dates there is no such pair.
     */
    private void determineDateTimeOfNewTask (ReadOnlyTask taskToEdit) {
    	
        if (dateMap.size() > 0) {
        	hasChanged = true;
        }
    	
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
        
        if(dateMap.containsKey(END_DATE)) {
            if (dateMap.get(END_DATE).equals(DateUtil.MARKER_FOR_DELETE)) {
            	dateMap.put(END_DATE, null);
            } 
        } else {
        	dateMap.put(END_DATE, null);
        }

        
        if (dateMap.containsKey(START_DATE)) {
        	if (dateMap.get(START_DATE).equals(DateUtil.MARKER_FOR_DELETE)) {
        		dateMap.put(START_DATE, null);
        	} 
        } else {
    		dateMap.put(START_DATE, null);
    	}
        
    
    }
    

    /* 
     * Updates the taglist
     */
    private Set<Tag> editOrDeleteTags(UniqueTagList currentTags, Set<String> tagNamesSet) throws IllegalValueException {
        
        assert model != null ;
        
        Set<Tag> newTaskTags = Sets.newHashSet(currentTags) ;
        
        for (String names : tagNamesSet) {
            
            Tag thisTag = model.getTagRegistry().getTagFromString(names, true) ;
            
            if (!currentTags.contains(thisTag)) {
                newTaskTags.add(thisTag) ;
                hasChanged = true ;
                
            } else {
                newTaskTags.remove(thisTag) ;
                hasChanged = true ;
            }
            
        }
        
        return newTaskTags ;
    }

    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(ImmutableList.copyOf(tasksAdded), ImmutableList.copyOf(tasksDeleted)) ; 
    }
}
