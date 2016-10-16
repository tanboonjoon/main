package guitests;

import org.junit.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.DeleteMessageBuilder;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS;

public class DeleteCommandTest extends TaskForceGuiTest {

    @Test
    public void delete() {

        //delete the first in the list
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);

        //delete the last in the list
        currentList = TestUtil.removeTaskFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);

        //delete from the middle of the list
        currentList = TestUtil.removeTaskFromList(currentList, targetIndex);
        targetIndex = currentList.length/2;
        assertDeleteSuccess(targetIndex, currentList);
        
        currentList = TestUtil.removeTaskFromList(currentList, targetIndex);
        assertDeleteSuccess(currentList, 1, 3);
        currentList = TestUtil.removeTaskFromList(currentList, 1);
        currentList = TestUtil.removeTaskFromList(currentList, 2);

        //invalid index
        commandBox.runCommand("delete " + currentList.length + 1);
        assertResultMessage("The task index provided is invalid");
        
        commandBox.runCommand("delete 1, aaaa");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

    }
    
    @Test
    public void deleteValidIndexWithInvalid() {

        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        assertDeleteSuccess(currentList, targetIndex, 2, 100000);
    }

    /**
     * Runs the delete command to delete the task at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        TestTask taskToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        TestTask[] expectedRemainder = TestUtil.removeTaskFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete.getName()));
    }
    
    /**
     * Runs the delete command to delete multiple tasks at specified indexes and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertDeleteSuccess(final TestTask[] currentList, int... targetIndexOneIndexed) {
        
        StringBuilder sb = new StringBuilder () ;
        TestTask[] expectedRemainder = currentList ;
        DeleteMessageBuilder builder = new DeleteMessageBuilder() ;
        
        for (int i = 0; i < targetIndexOneIndexed.length; i++) {
            
            if (targetIndexOneIndexed[i] <= currentList.length) {
                TestTask taskToDelete = currentList[targetIndexOneIndexed[i] - 1]; //-1 because array uses zero indexing
                expectedRemainder = TestUtil.removeTaskFromList(expectedRemainder, targetIndexOneIndexed[i] - i);
                builder.addDeletedTaskDetails(taskToDelete.getName());
            } else {
                builder.addIgnoredIndex(targetIndexOneIndexed[i]);
            }
            
            sb.append(targetIndexOneIndexed[i] + ", ") ;
        }
        
        String indexes = sb.toString() ;
        commandBox.runCommand("delete " + indexes.substring(0, indexes.length() - 2));

        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(builder.getDeleteCommandResultString());
    }

}
