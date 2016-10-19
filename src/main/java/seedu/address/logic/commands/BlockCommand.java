package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.model.Model;
import seedu.address.model.task.Block;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

// @@author A0135768R
public class BlockCommand extends Command {
    
    public static final String[] COMMAND_WORD = {
            "block",
            "reserve"
    };
    
    public static final String DEFAULT_COMMAND_WORD = COMMAND_WORD[0] ;
    public static final String MESSAGE_SUCCESS = "The following timeslots are reserved for %1$s: %2$s";
    
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the ToDo list!";
    public static final String INVALID_TASK_TYPE_MESSAGE = "Please make sure you follow the correct block format.";
    public static final String DATES_NOT_DISTINCT_MESSAGE = "Please make sure there are no overlapping starting and ending dates." ;
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
        
        if (!verifyAllDatesDistinct(startDates) || !verifyAllDatesDistinct(endDates) ) {
            throw new IllegalValueException(DATES_NOT_DISTINCT_MESSAGE);
        }
        
        this.name = name ;
        this.endDates = endDates ;
        this.startDates = startDates ;     
    }
    
    @Override
    public CommandResult execute() {
        
        assert endDates.size() == startDates.size() ;
        assert model != null ;
        
        int id = model.getNextTaskId() ;
        
        List<Block> blocksToAdd = new ArrayList<>(endDates.size()) ;
        
        for (int i = 0; i < endDates.size(); i ++) {
            blocksToAdd.add(i, new Block (id, name, startDates.get(i), endDates.get(i)));
        }
        
        Transaction transaction = new Transaction(blocksToAdd) ;
        
        StringBuilder sb = new StringBuilder() ;
        
        while (transaction.hasNext()) {
            try {
                Block addedTask = transaction.addNext(model) ;
                
                sb.append(DateUtil.parseLocalDateTimeIntoString(addedTask.getStartDate()) 
                        + " to " 
                        + DateUtil.parseLocalDateTimeIntoString(addedTask.getEndDate())) ;
                
                sb.append(" and ") ;
            
            } catch (DuplicateTaskException e) {
               
               transaction.rollback(model);
               return new CommandResult(MESSAGE_DUPLICATE_TASK) ;
            }
        }
        
        // Remove the last " and " 
        sb.delete(sb.length() - 5, sb.length()) ;
        
        return new CommandResult(String.format(MESSAGE_SUCCESS, name, sb.toString())) ;
    }
    
    /**
     * 
     * Verifies that all dates contains in the given list are unique and distinct.
     * In the case of empty or null list, this function will return false.
     * 
     * @param datesList     The list of dates
     * @return  true if all dates contained in the list are all unique and distinct; false otherwise.
     */
    private boolean verifyAllDatesDistinct (List<LocalDateTime> datesList) {
        
        if (datesList == null || datesList.isEmpty()) {
            return false ;
        }
        
        Set<LocalDateTime> datesInList = Sets.newHashSetWithExpectedSize(datesList.size()) ;
        
        for (LocalDateTime date : datesList) {
            if (!datesInList.contains(date)) {
                datesInList.add(date) ;
                continue ;
            }
            
            return false ;
        }
        
        return true ;
    }
    
    private static class Transaction {
        
        private List<Block> taskToAdd ;
        private int index ;
        
        public Transaction(List<Block> tasks) {
            this.taskToAdd = tasks ;
            this.index = 0 ;
        }
        
        public Block addNext (Model model) throws DuplicateTaskException {
            
            Block task = taskToAdd.get(index) ;
            
            model.addTask(task);
            index ++ ;
            
            return task ;
        }
        
        public boolean hasNext () {
            return index < taskToAdd.size() ;
        }
        
        public void rollback(Model model) {
            
            if (index == 0) {
                return ;
            }
            
            for (int i = index - 1; i >= 0; i --) {
                try {
                    model.deleteTask(taskToAdd.get(i));
                } catch (TaskNotFoundException e) {
                    continue ;
                }
            }
        }
    }

}
