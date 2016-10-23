package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import seedu.address.logic.commands.MarkCommand;
import seedu.address.testutil.TestTask;

public class MarkCommandTest extends TaskForceGuiTest {

    @Test
    public void mark() {
        TestTask[] currentList = td.getTypicalTasks();
        
        currentList[0].markAsDone() ;
        
      //  assetMarkSucess(currentList[0], 1, currentList) ;
        
        
    }
    
    private void assetMarkSucess (TestTask task, int index, final TestTask[] currentList) {
        commandBox.runCommand("mark " + index);
        
        TestTask[] newList = new TestTask[currentList.length] ;
        int j = 0 ;
        
        for (int i = 0; i < currentList.length; i ++) {
            
            if (i == index - 1) {
                newList[newList.length - 1] = currentList[i] ;
                continue ;
            }
            
            newList[j] = currentList[i] ;
            
            j++ ;
            
        }
        
        assertTrue(taskListPanel.isListMatching(newList));

        //confirm the result message is correct
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS_DONE, task));
    }

}
