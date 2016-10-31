package guitests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.testutil.TestTask;

public class MarkCommandTest extends TaskForceGuiTest {

    @Before
    public void clearList() {
        commandBox.runCommand("clear");
    }

    @Test
    public void mark() {
        TestTask[] currentList = td.getTypicalTasks();

        commandBox.runCommand("add task");
        commandBox.runCommand("mark 1");
        // currentList[0].markAsDone() ;

        // assetMarkSucess(currentList[0], 1, currentList) ;
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS_DONE, "task"));
        commandBox.runCommand("list");
        commandBox.runCommand("mark 1");
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS_UNDONE, "task"));
        commandBox.runCommand("mark 1001231232");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        commandBox.runCommand("clear");
        commandBox.runCommand("add deadline et/today 6pm");
        commandBox.runCommand("mark 1");
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS_DONE, "deadline"));
        commandBox.runCommand("add event st/today 6pm et/today 9pm");
        commandBox.runCommand("mark 2");
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS_DONE, "event"));

    }

    @After
    public void clear() {
        commandBox.runCommand("clear");
    }

}
