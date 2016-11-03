package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.address.logic.commands.EditCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class EditCommandTest extends TaskForceGuiTest {

    @Test
    public void editTest() {

        // Add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.hoon;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // Add second task
        taskToAdd = TypicalTestTasks.ida;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // Invalid commands
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        commandBox.runCommand("edit");
        assertResultMessage(expectedMessage);

        expectedMessage = EditCommand.NOTHING_CHANGED;
        commandBox.runCommand("edit 1");
        assertResultMessage(expectedMessage);

        // Edit first task
        commandBox.runCommand(TypicalTestTasks.elle.getEditCommand(1));
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // Edit second task
        commandBox.runCommand(TypicalTestTasks.fiona.getEditCommand(2));
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // Edit duplicate task
        // commandBox.runCommand(TypicalTestTasks.fiona.getEditCommand(3));
        // assertResultMessage(EditCommand.MESSAGE_DUPLICATE_TASK);

        // Edit invalid index
        commandBox.runCommand("edit 4000 " + TypicalTestTasks.alice);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        // edit invalid command
        commandBox.runCommand("Edits Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

    }

    // @@author A0111277M
    @Test
    public void editDates() {

        // Add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.hoon;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // add a deadline and starttime to task 1
        commandBox.runCommand("edit 8 st/2300 et/2359");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // able to remove end time
        commandBox.runCommand("edit 8 st/-");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        commandBox.runCommand("edit 8 et/-");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // remove endtime without removing starttime
        commandBox.runCommand("edit 8 st/2300 et/2359");
        commandBox.runCommand("edit 8 et/-");
        assertResultMessage(EditCommand.MESSAGE_CANNOT_HAVE_ST_ONLY);

        // can remove both at once
        commandBox.runCommand("edit 8 st/- et/-");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // add a block item 4
        commandBox.runCommand("block test st/2300 et/2359");

        // able to edit a block as per normal
        commandBox.runCommand("edit 9 nameChange");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // able to edit a block's date
        commandBox.runCommand("edit 9 et/2355");
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        // unable to remove a block's et or st
        commandBox.runCommand("edit 9 et/-");
        assertResultMessage(EditCommand.MESSAGE_BLOCK_CANNOT_REMOVE_DATE);

        commandBox.runCommand("edit 9 st/-");
        assertResultMessage(EditCommand.MESSAGE_BLOCK_CANNOT_REMOVE_DATE);

    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        // confirm the new card contains the right data
        TaskCardHandle AddedCard = taskListPanel.navigateToTask(taskToAdd.getName());
        assertMatching(taskToAdd, AddedCard);

        // confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
