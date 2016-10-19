package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import com.google.common.collect.Sets;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Adds a task to the taskForce list.
 */
public class AddCommand extends Command {

    public static final String[] COMMAND_WORD = {
            "add",
            "schedule"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;

    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD + ": Adds a task to the Todo list. \n"
    		+ "Format : Task : [TASKNAME] [d/DESCIPRTION] [t/TAG] ...\n" 
            + "Deadline : [TASKNAME] [d/DESCIPRTION] [et/END_DATE] [t/TAG] ...\n" 
    		+ "Event : [d/DESCRIPTION] [st/START_DATE] [et/END_DATE] [t/TAG] ...\n" 
            + "Example: " + DEFAULT_COMMAND_WORD
            + " Homework d/CS2103 hw t/veryImportant t/urgent";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the ToDo list!";
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please make sure you follow the correct add format";
    public static final String INVALID_END_DATE_MESSAGE = "Please make sure your end date is later than start date";
    
    private String name ;
    private String description ;
    private LocalDateTime startDate ;
    private LocalDateTime endDate ;
    private UniqueTagList tagList ;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String description,String startDate,String endDate, Set<String> tags) throws IllegalValueException {
        
        final Set<Tag> tagSet = Sets.newHashSet() ;

        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        if (startDate == null && endDate == null) {

            setNewTaskWithDetails(name, description, new UniqueTagList(tagSet));
        	
        } else if (startDate == null && endDate != null) {
        	
        	LocalDateTime deadline_endDate = DateUtil.parseStringIntoDateTime(endDate).isPresent() ?
        	        DateUtil.parseStringIntoDateTime(endDate).get() : DateUtil.END_OF_TODAY ;
        	
	        setNewTaskWithDetails(name, description, deadline_endDate, new UniqueTagList(tagSet));
        	
        } else if (startDate !=null) {
        	
        	LocalDateTime event_startDate = DateUtil.parseStringIntoDateTime(startDate).isPresent() ?
        	        DateUtil.parseStringIntoDateTime(startDate).get() : LocalDateTime.now() ;
        	        
	        LocalDateTime event_endDate = DateUtil.parseStringIntoDateTime(endDate).isPresent() ?
	                DateUtil.parseStringIntoDateTime(endDate).get() : DateUtil.END_OF_TODAY ;
        	
        	if (event_endDate.isBefore(event_startDate)) {
        		throw new IllegalValueException(INVALID_END_DATE_MESSAGE);
        	}
        	
        	setNewTaskWithDetails (name, description, event_startDate, event_endDate, new UniqueTagList(tagSet));
        	
        } else {
        	throw new IllegalValueException(INVALID_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        
        Task toAdd = getNewTask() ;
        model.recordTaskForce();
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
    }
    
    private void setNewTaskWithDetails (String name, String description, LocalDateTime startDate, LocalDateTime endDate, UniqueTagList tags) {
        this.name = name ;
        this.description = description ;
        this.startDate = startDate ;
        this.endDate = endDate ;
        this.tagList = tags ;
    }
    
    private void setNewTaskWithDetails (String name, String description, LocalDateTime endDate, UniqueTagList tags) {
        setNewTaskWithDetails(name, description, null, endDate, tags) ;
    }
    
    private void setNewTaskWithDetails (String name, String description, UniqueTagList tags) {
        setNewTaskWithDetails(name, description, null, null, tags) ;
    }
    
    private Task getNewTask () {
        
        assert model != null ;
        
        int id = model.getNextTaskId() ;
        
        if (startDate == null && endDate != null) {
            return new Deadline (id, name, description, endDate, tagList) ;
        }
        
        if (startDate != null && endDate != null) {
            return new Event (id, name, description, startDate, endDate, tagList) ;
        }
        
        return new Task (id, name, description, tagList) ;
    }

}
