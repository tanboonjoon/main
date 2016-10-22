package guitests;

import org.junit.Test;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;


public class UndoAndRedoCommandTest extends TaskForceGuiTest {

    @Test
    public void undo(){
        
        commandBox.runCommand("add event 1");
        commandBox.runCommand("add event 2");
        commandBox.runCommand("add event 3");
        commandBox.runCommand("add event 4");
        commandBox.runCommand("add event 5");
        commandBox.runCommand("add event 6");
        commandBox.runCommand("add event 7");
        commandBox.runCommand("add event 8");
        commandBox.runCommand("add event 9");
        commandBox.runCommand("add event 10");
        commandBox.runCommand("add event 11");

        // 
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        
        // undo more than 10 time not allowed.
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_NO_MORE_ACTION);
        
        
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_NO_MORE_ACTION);

        
        commandBox.runCommand("add test redo");
        commandBox.runCommand("add test redo clear");
        
        commandBox.runCommand("undo");
        commandBox.runCommand("add test redo clear clear");
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_NO_MORE_ACTION);
        //clear taskForce 
        commandBox.runCommand("clear");
    }

}


