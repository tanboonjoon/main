package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import javafx.util.Pair;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.logic.filters.TaskIdentifierNumberQualifier;
import seedu.address.model.tag.Tag;
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
    private final String startDate ;
    private final String endDate ;
    private final String description ;
    private final UniqueTagList taglist ;
    private final List<ReadOnlyTask> deletedTask, addedTask ;

    public ConfirmCommand (int targetIndex, String description, String startDate, String endDate, Set<String> tags) throws IllegalValueException {

        if (startDate == null || endDate == null) {
            throw new IllegalValueException(MESSAGE_DATES_NOT_NULL) ;
        }

        final Set<Tag> tagSet = Sets.newHashSet() ;

        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }

        this.targetIndex = targetIndex ;
        this.startDate = startDate ;
        this.endDate = endDate ;
        this.description = description ;
        this.taglist = new UniqueTagList(tagSet) ;
        
        this.deletedTask = new ArrayList<>() ;
        this.addedTask = new ArrayList<>() ;
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
        
        Optional<Pair<LocalDateTime, LocalDateTime>> datePair = DateUtil.determineStartAndEndDateTime(startDate, endDate) ;
        
        if (!datePair.isPresent()) {
            return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE)) ;
        }

        findAndDeleteOtherBlocks( (Block) blockToConfirm) ;

        String name = blockToConfirm.getName() ;
        
        Task newEvent = new Event(id, name, description, datePair.get().getKey(), datePair.get().getValue(), taglist) ;
        Optional<Event> conflict = DateUtil.checkForConflictingEvents(model, (Event) newEvent) ;

        try {
            model.addTask(newEvent) ;
            addedTask.add(newEvent) ;

        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK) ;
        }
        
        String successMessage = String.format(MESSAGE_CONFIRM_SUCCESS, newEvent) ;
        
        if ( conflict.isPresent() ) {
            successMessage = successMessage.concat("\n" + Messages.CONFLICTING_EVENTS_DETECTED + "The event is: " + conflict.get().getName()) ;
        }
        return new CommandResult(successMessage, true) ;
    }

    private void findAndDeleteOtherBlocks (Block task) {
        List<ReadOnlyTask> list = findAllOtherBlocks (task) ;

        for (ReadOnlyTask taskToDelete : list) {
            try {
                model.deleteTask(taskToDelete) ;
                deletedTask.add(taskToDelete) ;
                
            } catch (TaskNotFoundException e) {

                continue ;
            }
        }
    }

    private List<ReadOnlyTask> findAllOtherBlocks (Block task) {

        assert model != null ;

        int taskId = task.getTaskId() ;

        Expression filterByID = new PredicateExpression(new TaskIdentifierNumberQualifier(taskId)) ;
        model.searchTaskList(filterByID);

        return new ArrayList<>(model.getSearchedTaskList()) ;
    }
    
    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(ImmutableList.copyOf(addedTask), ImmutableList.copyOf(deletedTask)) ; 
    }
}
