package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import seedu.address.logic.commands.AddCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class AddCommandTest extends TaskForceGuiTest {

    @Test
    public void addCommandTest() {
        // add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.hoon;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // add another task
        taskToAdd = TypicalTestTasks.ida;

        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(TypicalTestTasks.alice);

        // invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        // invalid usage
        commandBox.runCommand("add");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // add a event that has endDate > startDate
        commandBox.runCommand("add testEvent st/today 6pm et/yesterday 6pm");
        assertResultMessage(AddCommand.INVALID_END_DATE_MESSAGE);

    }

    @Test
    public void addRecurringTest() {

        // invalid recurring argument.
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/anyhow repeat/2");
        assertResultMessage(AddCommand.WRONG_RECURRING_ARGUMENTS_MESSAGE);

        // invalid repeat argument negative number or less than minimum.
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/daily repeat/-3");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid repeat argument more than maximum number
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/daily repeat/21");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid repeat argument less than minimum of 1.
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/daily repeat/0");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid repeat argument space between number not allowed
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/daily repeat/3 4 5");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid repeat argument alphanumeric not allowed
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/daily repeat/abcd 12");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid repeat argument alphanumeric not allowed
        commandBox.runCommand("add testRecurringEvent st/now et/tomorrow 6pm recurring/daily repeat/abcd 12");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);

        // invalid command
        commandBox.runCommand("add test task et/now repeat/");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/anyhow");
        assertResultMessage(AddCommand.MISSING_NUMBER_OF_RECURRENCE_MESSAGE);

        // missing recurring/

        commandBox.runCommand("add test task et/now repeat/2");
        assertResultMessage(AddCommand.MESSAGE_USAGE);

        // invalid repeat argument alphanumeric not allowed
        commandBox.runCommand("add testRecurringDeadline et/tomorrow 6pm recurring/weekly repeat/abcd 12");
        assertResultMessage(AddCommand.REPEAT_ARGUMENT_MESSAGE);
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        // confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName());
        assertMatching(taskToAdd, addedCard);

        // confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
