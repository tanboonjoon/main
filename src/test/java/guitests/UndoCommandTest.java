package guitests;

import org.junit.Test;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalTestTasks;
import seedu.address.ui.CommandBox;

public class UndoCommandTest extends TaskForceGuiTest {

    @Test
    public void undo(){
        
        //Add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = TypicalTestTasks.hoon;
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        commandBox.runCommand("undo");
        assertResultMessage("");
    }

//    private void assertUndoCommandSuccess() {
//        commandBox.runCommand("undo");
//        assertListSize(0);
//        assertResultMessage("Undo");
//    }
}


