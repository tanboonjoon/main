package guitests;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.testutil.TestTask;

import static org.junit.Assert.assertTrue;

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



    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
