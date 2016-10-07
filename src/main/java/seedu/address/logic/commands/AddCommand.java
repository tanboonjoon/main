package seedu.address.logic.commands;

import java.util.Set;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Adds a task to the taskForce list.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: NAME d/DESCRIPTION [e/TAG] ...\n"
            + "Example: " + COMMAND_WORD
            + " Homework d/CS2103 hw e/veryImportant e/urgent";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String description, Set<String> tags) throws IllegalValueException {
        final Set<Tag> tagSet = Sets.newHashSet() ;
        
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        this.toAdd = new Task(name, description, new UniqueTagList(tagSet));
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
