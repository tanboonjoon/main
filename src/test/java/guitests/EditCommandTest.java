package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class EditCommandTest extends TaskForceGuiTest {

    @Test
    public void Edit() {
        
        //Add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.hoon;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //Add second task
        taskToAdd = TypicalTestTasks.ida;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        //Add third task
        taskToAdd = TypicalTestTasks.benson;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        // Invalid commands
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        commandBox.runCommand("edit") ;
        assertResultMessage(expectedMessage);
        
        expectedMessage = EditCommand.NOTHING_CHANGED;
        commandBox.runCommand("edit 1") ;
        assertResultMessage(expectedMessage);
        
        //Edit first task
        commandBox.runCommand(TypicalTestTasks.elle.getEditCommand(1));
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);

        //Edit second task
        commandBox.runCommand(TypicalTestTasks.fiona.getEditCommand(2));
        assertResultMessage(EditCommand.MESSAGE_EDIT_SUCCESS);
        
        //Edit duplicate task
//       commandBox.runCommand(TypicalTestTasks.fiona.getEditCommand(3));
//       assertResultMessage(EditCommand.MESSAGE_DUPLICATE_TASK);

        //Edit invalid index
        commandBox.runCommand("edit 4000 "+TypicalTestTasks.alice);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
        //edit invalid command
        commandBox.runCommand("Edits Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);


        
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle AddedCard = taskListPanel.navigateToTask(taskToAdd.getName());
        assertMatching(taskToAdd, AddedCard);

        //confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
   


}
