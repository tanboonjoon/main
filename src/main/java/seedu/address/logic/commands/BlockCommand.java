package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;

public class BlockCommand extends Command {
    
    public static final String[] COMMAND_WORD = {
            "block",
            "reserve"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;
    public static final String MESSAGE_SUCCESS = "The following timeslots are reserved for %1$s: %2$s";
    
    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD + ": Reserves timeslots for an event that is unconfirmed. \n" 
            + "Format: Block EVENT_NAME st/START TIME et/END TIME [st/START TIME et/END_TIME]..."
            + "Example: block meeting with boss st/today 2pm et/today 4pm st/tommorrow 2pm et/tommorrow 4pm" ;
    
    
    public BlockCommand (String name, List<LocalDateTime> startDates, List<LocalDateTime> endDates) {
        
    }
    
    @Override
    public CommandResult execute() {
        return null ;
    }

}
