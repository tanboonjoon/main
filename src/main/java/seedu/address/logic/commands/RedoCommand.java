package seedu.address.logic.commands;

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redid the most recent undone comment.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": redo the undo action.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
    
        if(!model.restoreTaskForce()){
            return new CommandResult("No further action to redo.");
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
