package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.logic.filters.TaskIdentifierNumberQualifier;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Block;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;


/**
 * @@author A0135768R
 * 
 * A command to confirm a previously blocked out timeslot(s)
 *
 */
public class ConfirmCommand extends Command {

    public static final String COMMAND_WORD = "confirm" ;

    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the ToDo list!";
    public static final String MESSAGE_CONFIRM_SUCCESS = "The following event is successfully confirmed: %1$s" ;
    public static final String MESSAGE_DATES_NOT_NULL = "Please enter in a pair of valid start and end dates/times!" ;
    public static final String MESSAGE_ONLY_BLOCKS = "You can only confirm an unconfirmed event!" ;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Confirms a previously blocked timeslot into an event. \n" 
            + "The start and end date provided is the confirmed dates. \n"
            + "Format: confirm INDEX st/START TIME et/END TIME [d/DESCRIPTION] [t/tag...] \n"
            + "Example: confirm meeting with boss st/today 2pm et/today 4pm" ;

    private final int targetIndex ;
    private final LocalDateTime startDate ;
    private final LocalDateTime endDate ;
    private final String description ;
    private final UniqueTagList taglist ;

    public ConfirmCommand (int targetIndex, String description, LocalDateTime startDate, LocalDateTime endDate, UniqueTagList tags) throws IllegalValueException {
        
        if (startDate == null || endDate == null) {
            throw new IllegalValueException(MESSAGE_DATES_NOT_NULL) ;
        }
        
        this.targetIndex = targetIndex ;
        this.startDate = startDate ;
        this.endDate = endDate ;
        this.description = description ;
        this.taglist = tags ;
    }

    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex || targetIndex < 1) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask blockToConfirm = lastShownList.get(targetIndex - 1);  
        int id = model.getNextTaskId() ;

        if (!(blockToConfirm instanceof Block) ) {

            return new CommandResult(MESSAGE_ONLY_BLOCKS) ;
        }
        
        findAndDeleteOtherBlocks( (Block) blockToConfirm) ;
        
        String name = blockToConfirm.getName() ;
        
        Task newEvent = new Event(id, name, description, startDate, endDate, taglist) ;
        
        try {
            model.addTask(newEvent);
        
        } catch (DuplicateTaskException e) {         
            return new CommandResult(MESSAGE_DUPLICATE_TASK) ;
        }

        return new CommandResult(String.format(MESSAGE_CONFIRM_SUCCESS, newEvent)) ;
    }
    
    private void findAndDeleteOtherBlocks (Block task) {
        List<ReadOnlyTask> list = findAllOtherBlocks (task) ;
        
        for (ReadOnlyTask taskToDelete : list) {
            try {
                model.deleteTask(taskToDelete) ;
            } catch (TaskNotFoundException e) {
                continue ;
            }
        }
    }

    private List<ReadOnlyTask> findAllOtherBlocks (Block task) {
        
        assert model != null ;

        int taskId = task.getTaskId() ;

        Expression filterByID = new PredicateExpression(new TaskIdentifierNumberQualifier(taskId)) ;
        model.updateFilteredTaskList(filterByID);
        
        return model.getFilteredTaskList() ;
    }

}

