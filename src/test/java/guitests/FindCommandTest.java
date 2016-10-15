package guitests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.testutil.TestTask;

public class FindCommandTest extends TaskForceGuiTest {
       
    @Test
    public void find_validCommand_pass() {
    	commandBox.runCommand("find task all/taskName");
    	commandBox.runCommand("find task day/0");
    	commandBox.runCommand("find task week/0");
    }
    
    @Test
    public void find_invalidCommand_fail() {
    	commandBox.runCommand("find task day/123 sdf");
    	assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
    	commandBox.runCommand("find task week/thisIsNotNumber");
    	assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
    	commandBox.runCommand("find task all/");
    	String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);

    	assertResultMessage(expectedMessage);
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find task all/Jean"); //no results
    }
    
    @Test
    public void find_MinimalValidCommand_pass() {
        
        commandBox.runCommand("clear");
        
        List<TestTask> list = populateTestData() ;
        
        for (TestTask task : list) {
            commandBox.runCommand("add " + task.getName());
        }
        
        TestTask[] array = new TestTask[list.size()] ;
        
        assertFindResult("find all/john", list.toArray(array));
        commandBox.runCommand("find day/0");
    }



    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
    
    private List<TestTask> populateTestData() {
        List<TestTask> list = Lists.newLinkedList() ;
        
        for (int i = 0; i < 5; i ++) {
            TestTask task = new TestTask () ;
            task.setName("john " + i);
            
            list.add(task) ;
        }
        
        return list ;
    }
}
