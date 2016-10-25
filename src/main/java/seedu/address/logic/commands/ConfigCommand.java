package seedu.address.logic.commands;

public class ConfigCommand extends Command {
    
    public static final String COMMAND_WORD = "config";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the given config option to the given value \n"
            + "Format: config CONFIG_OPTION v/NEW VALUE \n"
            + "Example: config activeHoursFrom v/1000" ;
    
    @Override
    public CommandResult execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
