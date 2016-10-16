package seedu.address.logic.commands;

import java.util.List;

import com.google.common.collect.Lists;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Deletes a task identified using it's last displayed index from the taskForce list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";

    public final List<Integer> targetIndexes;

    public DeleteCommand(int... targetIndex) {
        this.targetIndexes = Lists.newLinkedList() ;
        
        for (int index : targetIndex) {
            this.targetIndexes.add(index) ;
        }
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        
        List<Integer> tasksToDelete = Lists.newLinkedList() ;
        List<Integer> taskNotDeleted = Lists.newLinkedList() ;
        
        for (int targetIndex : targetIndexes) {
            if (lastShownList.size() < targetIndex) {
                taskNotDeleted.add(targetIndex) ;
            } else {
                tasksToDelete.add(targetIndex) ;
            }
        }
        
        if (tasksToDelete.isEmpty()) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        for (int targetIndex : tasksToDelete) {
            ReadOnlyTask taskToDelete = lastShownList.get(targetIndex - 1);
    
            try {
                model.deleteTask(taskToDelete);
            } catch (TaskNotFoundException pnfe) {
                assert false : "The target task cannot be missing";
            }
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS));
    }

}
