package guitests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandBoxTest extends TaskForceGuiTest {

    @Test
    public void commandBox_commandSucceeds_textCleared() {
        commandBox.runCommand(td.benson.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
    }

    @Test
    public void commandBox_commandFails_textStays() {
        commandBox.runCommand("invalid command");
        assertEquals(commandBox.getCommandInput(), "invalid command");
        // TODO: confirm the text box color turns to red
    }

    // @@author A0140037W
    @Test
    public void commandBox_UpDownArrowKey_retrieveStoredCommandText() {
        commandBox.runCommand(td.benson.getAddCommand());
        commandBox.pressUpArrow();
        assertEquals(commandBox.getCommandInput(), td.benson.getAddCommand());
        commandBox.runCommand(td.carl.getAddCommand());
        commandBox.pressUpArrow();
        assertEquals(commandBox.getCommandInput(), td.carl.getAddCommand());
        commandBox.pressUpArrow();
        assertEquals(commandBox.getCommandInput(), td.benson.getAddCommand());
        commandBox.pressDownArrow();
        commandBox.pressDownArrow();
        assertEquals(commandBox.getCommandInput(), td.carl.getAddCommand());
    }

}
