package seedu.address.logic.commands;

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo ";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": redo the undo action.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
    
        if(!model.restoreTaskForce()){
            return new CommandResult("Execuse me, you want redo till where?");
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
