package guitests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.testutil.TestTask;

public class FindCommandTest extends TaskForceGuiTest {

    @Test
    public void find_validCommand_pass() {
    	commandBox.runCommand("find all/taskName");
    	commandBox.runCommand("find day/0");
    	commandBox.runCommand("find week/0");
    	commandBox.runCommand("add task123 st/next week 4pm et/next month 5pm");
    	commandBox.runCommand("add task123 st/today 4pm et/next week 5pm");
    	commandBox.runCommand("find week/1");
    }
    
    @Test
    public void find_invalidCommand_fail() {
    	commandBox.runCommand("find day/123 sdf");
    	assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
    	commandBox.runCommand("find week/thisIsNotNumber");
    	assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
    	commandBox.runCommand("find all/");
    	String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
    	assertResultMessage(expectedMessage);
    	commandBox.runCommand("find asdf day/123" );
    	assertResultMessage(expectedMessage);


    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find all/Jean"); //no results
    }
    
    @Test
    public void find_MinimalValidCommand_pass() {
        
        commandBox.runCommand("clear");
        
        List<TestTask> list = populateTestData() ;
        
        for (TestTask task : list) {
            StringBuilder sb = new StringBuilder() ;
            
            sb.append("add " + task.getName()) ;
            
            if (task.getEndDate() != null) {
                sb.append(" et/" + task.getEndDate().toString()) ;
            }
            
            if (task.getStartDate() != null) {
                sb.append(" st/" + task.getStartDate().toString()) ;
            }
            
            commandBox.runCommand(sb.toString());
        }
        
        TestTask[] array = new TestTask[list.size()] ;
        
        assertFindResult("find all/john", list.toArray(array));
        
        assertFindResult("find day/0", list.get(0), list.get(1), list.get(2));
        assertFindResult("find day/1", list.get(3));
        assertFindResult("find week/1", list.get(4));
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
            
            if (i < 3) {
                task.setEndDate(DateUtil.END_OF_TODAY);
            
            } else if (i >= 3 && i < 4) {
                task.setEndDate(DateUtil.parseStringIntoDateTime("tomorrow").get());
            
            } else {
                task.setEndDate(DateUtil.parseStringIntoDateTime("next week").get());
            }
            
            list.add(task) ;
        }
        
        return list ;
    }
}
