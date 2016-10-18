package seedu.address.logic.commands;

public class BlockCommand extends Command {
    
    public static final String[] COMMAND_WORD = {
            "block",
            "reserve"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;

    @Override
    public CommandResult execute() {
        return null ;
    }

}
