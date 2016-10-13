package seedu.address.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;


import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Parser parser;
    private final Map<Model, String> history = new LinkedHashMap<Model, String>();
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult invoke(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        command.setHistory(history);
        CommandResult result = null;
  
        if (command.isUndoableCommand()) {
            command.history.put(model, commandText);
            result = command.execute();
        }else{
            result = command.execute();
        }
        
        

        
        BaseEvent commandExecuted = new TaskForceCommandExecutedEvent(command.getClass(), commandText, result) ;
        
        model.raiseEvent(commandExecuted) ;
        return result ;
        
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
}
