package seedu.address.logic.commands;

import java.util.Collections;
import java.util.List;

import javafx.util.Pair;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;

/**
 * Represents a command with hidden internal logic and the ability to be
 * executed.
 */
public abstract class Command {
    protected Model model;

    /**
     * Constructs a feedback message to summarise an operation that displayed a
     * listing of task.
     *
     * @param displaySize
     *            used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command. Commands making use of
     * any of these should override this method to gain access to the
     * dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }

    /**
     * Returns the pair of list detailing the changes made to the TaskForce by
     * this command.
     * <p>
     * 
     * The List in the key represents the list of ADDITIONS made by this
     * command. <br>
     * The List in the value represents the list of DELETIONS made by this
     * command.
     * <p>
     * 
     * If no changes are made, an empty list should be returned.
     * 
     * @return list of tasks added as the key, and list of tasks removed as the
     *         value.
     */
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
}
