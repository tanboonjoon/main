/**
 * @@author A0140037W
 */
package seedu.address.logic.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javafx.util.Pair;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redid the most recent undone comment.";
    public static final String MESSAGE_NO_MORE_ACTION = "No further action to redo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": redo the undo action.\n"
            + "Example: " + COMMAND_WORD;
    private List<ReadOnlyTask> tasksAdded = Lists.newLinkedList();
    private List<ReadOnlyTask> tasksDeleted = Lists.newLinkedList();
    
    @Override
    public CommandResult execute() {
        TaskForceCommandExecutedEvent changes = (TaskForceCommandExecutedEvent) model.restoreChanges();
        if(changes != null){
            Command cmd = ((TaskForceCommandExecutedEvent) changes).commandInstance;
            List<ReadOnlyTask> toDelete = cmd.getCommandChanges().getKey();
            List<ReadOnlyTask> toAdd = cmd.getCommandChanges().getValue();
            if(toDelete.isEmpty() && toAdd.isEmpty()){
                return new CommandResult(MESSAGE_NO_MORE_ACTION);
            }
            for(ReadOnlyTask add: toAdd){
                try {
                    model.addTask((Task) add);
                    tasksAdded.add(add);

                } catch (DuplicateTaskException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            for(ReadOnlyTask remove: toDelete){
                try {
                    model.deleteTask(remove);
                    tasksDeleted.add(remove);

                } catch (TaskNotFoundException e) {
                    assert false: "task not found";
                    e.printStackTrace();
                }
            }
            
            return new CommandResult(MESSAGE_SUCCESS);

        }
        return new CommandResult(MESSAGE_NO_MORE_ACTION);

    }
    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(ImmutableList.copyOf(tasksAdded), ImmutableList.copyOf(tasksDeleted)) ; 
    }
}
