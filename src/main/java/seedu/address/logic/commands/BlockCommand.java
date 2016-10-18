package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;

public class BlockCommand extends Command {
    
    public static final String[] COMMAND_WORD = {
            "block",
            "reserve"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;
    public static final String MESSAGE_SUCCESS = "The following timeslots are reserved for %1$s: %2$s";
    
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please make sure you follow the correct block format.";
    public static final String MESSAGE_USAGE = DEFAULT_COMMAND_WORD + ": Reserves timeslots for an event that is unconfirmed. \n" 
            + "Every start date must have a corresponding end date \n"
            + "Format: Block EVENT_NAME st/START TIME et/END TIME [st/START TIME et/END_TIME]... \n"
            + "Example: block meeting with boss st/today 2pm et/today 4pm st/tommorrow 2pm et/tommorrow 4pm" ;
    
    private final String name ;
    private final List<LocalDateTime> startDates ;
    private final List<LocalDateTime> endDates ;
    
    
    public BlockCommand (String name, List<LocalDateTime> startDates, List<LocalDateTime> endDates) throws IllegalValueException {
        
        if (startDates == null || endDates == null) {
            throw new IllegalValueException(INVALID_TASK_TYPE_MESSAGE);
        }
        
        this.name = name ;
        this.endDates = endDates ;
        this.startDates = startDates ;     
    }
    
    @Override
    public CommandResult execute() {
        
        assert endDates.size() == startDates.size() ;
        
        return null ;
    }

}
