package seedu.address.logic.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.collect.Iterables;

import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command {
    
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undid the most recent command.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": undo the previous action.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
    
        if(!model.revertTaskForce()){
            return new CommandResult("No further action to undo.");
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
