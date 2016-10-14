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
    public static final String MESSAGE_SUCCESS = "Undo ";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": undo the previous action.\n"
            + "Parameters: STEP (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    @Override
    public CommandResult execute() {
    
        if(!model.revertTaskForce()){
            return new CommandResult("Execuse me, you want undo till where?");
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
