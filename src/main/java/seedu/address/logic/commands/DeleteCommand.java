package seedu.address.logic.commands;

import java.util.List;

import com.google.common.collect.Lists;

import javafx.util.Pair;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * @@author A0135768R
 * 
 * Deletes a task identified using it's last displayed index from the taskForce list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task(s): %1$s ";
    public static final String MESSAGE_DELETE_TASK_NOT_FOUND = "Task(s) not found: %1$s ";
    public static final String MESSAGE_DELETE_TASK_IGNORED = "The following indexes are invalid and ignored: %1$s ";

    public final List<Integer> targetIndexes;

    public DeleteCommand(Integer... targetIndex) {
        this.targetIndexes = Lists.newLinkedList() ;
        
        for (int index : targetIndex) {
            this.targetIndexes.add(index) ;
        }
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        
        List<ReadOnlyTask> tasksToDelete = Lists.newLinkedList() ;
        
        DeleteMessageBuilder messageBuilder = new DeleteMessageBuilder() ;
        
        for (int targetIndex : targetIndexes) {
            if (lastShownList.size() < targetIndex) {
                messageBuilder.addIgnoredIndex(targetIndex) ;
            } else {
                tasksToDelete.add(lastShownList.get(targetIndex - 1)) ;
            }
        }
        
        if (tasksToDelete.isEmpty()) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        model.recordTaskForce();
        
        for (ReadOnlyTask task : tasksToDelete) {
    
            try {
                model.deleteTask(task);
                
                messageBuilder.addDeletedTaskDetails(task.getName()) ;
            } catch (TaskNotFoundException pnfe) {
                messageBuilder.addTaskNotFound(task.getName());
            }
        }
        
     
        return new CommandResult(messageBuilder.getDeleteCommandResultString());
    }
    
    /**
     * 
     * A specialized class to build the success/failure message for the delete command
     *
     */
    public static class DeleteMessageBuilder {
        
        private StringBuilder deletedTask ;
        private StringBuilder ignoredIndexes ;
        private StringBuilder tasksNotFound ;
        
        public DeleteMessageBuilder () {
            this.deletedTask = new StringBuilder () ;
            this.ignoredIndexes = new StringBuilder () ;
            this.tasksNotFound = new StringBuilder () ;
        }
        
        public void addDeletedTaskDetails (String taskName) {
            deletedTask.append(taskName + " and ") ;
        }
        
        public void addIgnoredIndex (int index) {
            ignoredIndexes.append(index) ;
        }
        
        public void addTaskNotFound (String taskName) {
            tasksNotFound.append(taskName + " and ") ;
        }
        
        public String getDeletedTasks () {
            String result = deletedTask.toString() ;
            
            if (result.length() > 0) {
                return result.substring(0, result.length() - 5) ;
            }
            
            return "" ;
        }
        
        public String getIgnoredIndexes () {
            return ignoredIndexes.toString() ;
        }
        
        public String getTasksNotFound () {
            String result = tasksNotFound.toString() ;
            
            if (result.length() > 0) {
                return result.substring(0, result.length() - 5) ; 
            }
            
            return "" ;
        }
        
        public String getDeleteCommandResultString () {
            StringBuilder sb = new StringBuilder() ;
            
            String taskNotFound = getTasksNotFound() ;
            String indexesIgnored = getIgnoredIndexes() ;
            String deletedTasks = getDeletedTasks() ;
            
            if (deletedTasks.length() > 0) {
                sb.append(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTasks)) ;
            }

            if (taskNotFound.length() > 0) {
                sb.append("\n") ;
                sb.append(String.format(MESSAGE_DELETE_TASK_NOT_FOUND, taskNotFound)) ;
            }
            
            if (indexesIgnored.length() > 0) {
                sb.append("\n") ;
                sb.append(String.format(MESSAGE_DELETE_TASK_IGNORED, indexesIgnored)) ;
            }
            
            return sb.toString() ;
        }
    }

    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        // TODO Auto-generated method stub
        return null;
    }
    

}
