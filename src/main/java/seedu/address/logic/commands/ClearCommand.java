package seedu.address.logic.commands;

import seedu.address.model.TaskForce;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String WARNING_MESSAGE = "WARNING! Once clear cannot be recovered. [Y/n]?";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskForce.getEmptyTaskForce());
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
}
