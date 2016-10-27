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
        
        // @@author A0111277M
        // testing undo upon other commands

        commandBox.runCommand("add event");
        
        // can undo edits
        commandBox.runCommand("edit 1 st/0000 et/2359");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo mark
        commandBox.runCommand("mark 1");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo delete
        commandBox.runCommand("delete 1");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo multiple delete
        commandBox.runCommand("add another event");
        commandBox.runCommand("delete 1, 2");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo block
        commandBox.runCommand("redo");
        commandBox.runCommand("block blocker st/1000 et/1100 st/1200 et/1300");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo edit block
        commandBox.runCommand("redo");
        commandBox.runCommand("edit 1 st/0000 et/2359");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        // can undo confirm
        commandBox.runCommand("confirm 1 st/0000 et/2359");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
        
        commandBox.runCommand("clear");
    }

}


