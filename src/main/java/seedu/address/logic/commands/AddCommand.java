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
import seedu.address.commons.util.StringUtil;
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

    private static final int RECURRENCE_ALTERNATE_INCREMENT_STEP = 2;
    private static final int RECURRENCE_INCREMENT_STEP = 1;
    private static final int MIN_NUMBER_OF_RECURRENCE = 1;
    private static final int MAX_NUMBER_OF_RECURRENCE = 20;
    public static final String[] COMMAND_WORD = {
            "add",
            "schedule",
            "remind"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;

    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD + ": Adds a person to the Task Force. \n"
    		+ "Format : Task : TASKNAME [d/DESCIPRTION] [t/TAG...]\n" 
                + "Deadline : TASKNAME [d/DESCIPRTION] et/END_DATE [t/TAG...]\n" 
    		+ "Event : EVENTNAME [d/DESCRIPTION] st/START_DATE et/END_DATE [t/TAG...]\n" 
                + "Recurring Event : EVENTNAME [d/DESCRIPTION] st/START_DATE et/END_DATE recurring/weekly repeat/12 [t/TAG...]\n"
            + "Example: " + DEFAULT_COMMAND_WORD
            + " Homework d/CS2103 hw t/veryImportant t/urgent";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please make sure you follow the correct add format";
    public static final String INVALID_END_DATE_MESSAGE = "Please make sure your end date is later than start date";
    public static final String MISSING_NUMBER_OF_RECURRENCE_MESSAGE = "Please indicate the number of recurring by using 'repeat/NUMBER (between 1 - 20)'";
    private static final String REPEAT_ARGUMENT_MESSAGE = "repeat argument must be positive integer between 1 and 20.";
    private static final String WRONG_RECURRING_ARGUMENTS_MESSAGE = "Wrong usage of recurring argument. There are 8 options: daily, weekly, monthly, yearly, alternate day, alternate week, alternate month and alternate year.";

    private String name ;
    private String description ;
    private LocalDateTime startDate ;
    private LocalDateTime endDate ;
    private UniqueTagList tagList ;
    private String recurringFrequency;
    private int repeat;
    private int id;
    
    private ArrayList<Task> taskList = new ArrayList<Task>();

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */

    public AddCommand(String name, String description,String startDate,String endDate, Set<String> tags, String recurring, String repeat) throws IllegalValueException {
        final Set<Tag> tagSet = Sets.newHashSet();
        
        setRecurrenceAttributes(recurring, repeat);
        
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
        this.id = model.getNextTaskId();
        
        if(recurringFrequency != null && repeat == 0) {
            return new CommandResult(MISSING_NUMBER_OF_RECURRENCE_MESSAGE);
        }else if(recurringFrequency != null && repeat >= MIN_NUMBER_OF_RECURRENCE){
                try {
                    this.createRecurringEvent(recurringFrequency, repeat);
                } catch (IllegalValueException e) {
                    return new CommandResult(e.getMessage());
                }
        }else{
            this.taskList.add(getNewTask());
        }
        
        try {
            for(Task e: taskList){
                model.addTask(e);
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, taskList.get(0)));
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

        if (startDate == null && endDate != null) {
            return new Deadline (id, name, description, endDate, tagList) ;
        }
        
        if (startDate != null && endDate != null) {
            return new Event (id, name, description, startDate, endDate, tagList) ;
        }
        
        return new Task (id, name, description, tagList) ;
    }
    
// @@author A0140037W    
    
    private void createRecurringEvent(String recurring, int repeat) throws IllegalValueException {
        if(repeat >= MIN_NUMBER_OF_RECURRENCE){
            this.taskList.add(getNewTask());
            this.startDate = this.parseFrequency(startDate, recurring);
            this.endDate = this.parseFrequency(endDate, recurring);
            this.createRecurringEvent(recurring,repeat-1);
        }
            
    }
    
    private LocalDateTime parseFrequency(LocalDateTime date, String recurring) throws IllegalValueException {
        if(date != null) {
            switch(recurring.trim().toLowerCase()){
            case "daily":
                return date.plusDays(RECURRENCE_INCREMENT_STEP);
            case "weekly":
                return date.plusWeeks(RECURRENCE_INCREMENT_STEP);
            case "monthly":
                return date.plusMonths(RECURRENCE_INCREMENT_STEP);
            case "yearly":
                return date.plusYears(RECURRENCE_INCREMENT_STEP);
            case "alternate day":
                return date.plusDays(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "alternate week":
                return date.plusWeeks(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "alternate month":
                return date.plusMonths(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            case "alternate year":
                return date.plusYears(RECURRENCE_ALTERNATE_INCREMENT_STEP);
            default:
                throw new IllegalValueException(WRONG_RECURRING_ARGUMENTS_MESSAGE);
            }
        }else{
            return date;
        }
        
    }
    

    private void setRecurrenceAttributes(String recurring, String repeat) throws IllegalValueException {
        if(recurring != null && repeat == null){
            throw new IllegalValueException(MISSING_NUMBER_OF_RECURRENCE_MESSAGE);
        }else if(recurring == null && repeat != null){
            throw new IllegalValueException(MESSAGE_USAGE);
        }else if(recurring != null){
            this.recurringFrequency = recurring;
        }
        

        setRepeat(repeat);
    }


    private void setRepeat(String repeat) throws IllegalValueException {
        if(repeat != null){
            if(StringUtil.isParsable(repeat) && StringUtil.isUnsignedInteger(repeat)){
                int temp = Integer.parseInt(repeat);
                if(temp <= MAX_NUMBER_OF_RECURRENCE && temp >= MIN_NUMBER_OF_RECURRENCE){
                    this.repeat = Integer.parseInt(repeat);
                }else{
                    throw new IllegalValueException(REPEAT_ARGUMENT_MESSAGE);
                }
            }
            else{
                throw new IllegalValueException(REPEAT_ARGUMENT_MESSAGE);
            }
        }
    }

}
