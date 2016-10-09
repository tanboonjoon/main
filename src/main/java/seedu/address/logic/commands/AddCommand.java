package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Adds a task to the taskForce list.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: NAME [d/DESCRIPTION] [st/START_DATE] [et/END_DATE] [e/TAG] ...\n"
            + "Example: " + COMMAND_WORD
            + " Homework d/CS2103 hw t/veryImportant t/urgent";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please add a endDate as well OR remove startDate from your command";
    private Task toAdd;
    private DateTimeFormatter formatter;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String description,String startDate,String endDate, Set<String> tags) throws IllegalValueException {
        final Set<Tag> tagSet = Sets.newHashSet() ;
        formatter = DateTimeFormatter.ofPattern("ddMMyyy HHmm");
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        if (startDate == null && endDate == null) {

        	this.toAdd = new Task(name, description, new UniqueTagList(tagSet));
        	
        }else if (startDate == null && endDate != null) {
        	
        	LocalDateTime deadline_endDate = LocalDateTime.parse(endDate, formatter);
        	this.toAdd = new Deadline(name, description, deadline_endDate, new UniqueTagList(tagSet));
        	
        }else if (startDate !=null && endDate != null) {
        	
        	LocalDateTime event_startDate = LocalDateTime.parse(startDate, formatter);
        	LocalDateTime event_endDate = LocalDateTime.parse(endDate, formatter);
        	this.toAdd = new Event(name, description, event_startDate, event_endDate, new UniqueTagList(tagSet));
        }else{
        	throw new IllegalValueException(INVALID_TASK_TYPE_MESSAGE);
        }
        
        
        
        
        
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

    }

}
