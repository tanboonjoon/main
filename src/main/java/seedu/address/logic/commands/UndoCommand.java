package seedu.address.logic.commands;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Iterables;

import seedu.address.model.Model;

public class UndoCommand extends Command {
    
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo ";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": undo the previous action.\n"
            + "Parameters: STEP (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    @Override
    public CommandResult execute() {

        Set<Model> models = this.history.keySet();
        this.model = Iterables.getLast(models, null);
        String command = this.history.get(this.model);
        return new CommandResult(MESSAGE_SUCCESS+command);
    }
    
    @Override
    public boolean isUndoableCommand(){
        return true;
    }

}
