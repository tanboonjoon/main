package seedu.address.logic.commands;


public class UndoCommand extends Command {
    
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undid the most recent command.";
    public static final String MESSAGE_NO_MORE_ACTION = "No further action to undo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": undo the previous action.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
    
        if(!model.revertTaskForce()){
            return new CommandResult(MESSAGE_NO_MORE_ACTION);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
