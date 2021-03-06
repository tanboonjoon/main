package seedu.address.logic;

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

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        Command command = parser.parseCommand(commandText);
        command.setData(model);

        CommandResult result = command.execute();
        BaseEvent commandExecuted = new TaskForceCommandExecutedEvent(command, result);

        model.raiseEvent(commandExecuted);

        return result;

    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return getSortedFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getSortedFilteredTaskList() {
        return model.getSortedFilteredTask();
    }

    public ObservableList<ReadOnlyTask> getInitialTodaysTaskList() {
        return model.startWithTodaysTasks();
    }
}
