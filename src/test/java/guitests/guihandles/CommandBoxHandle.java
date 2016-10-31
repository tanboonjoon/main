package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * A handle to the Command Box in the GUI.
 */
public class CommandBoxHandle extends GuiHandle {

    private static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public void enterCommand(String command) {
        setTextField(COMMAND_INPUT_FIELD_ID, command);
    }

    public String getCommandInput() {
        return getTextFieldText(COMMAND_INPUT_FIELD_ID);
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     */
    public void runCommand(String command) {
        enterCommand(command);
        pressEnter();
        if ("clear".equals(command)) {
            pressEnter();
        }
        guiRobot.sleep(200); // Give time for the command to take effect
    }

    public HelpWindowHandle runHelpCommand() {
        enterCommand("help");
        pressEnter();
        return new HelpWindowHandle(guiRobot, primaryStage);
    }

    public void pressUpArrow() {
        guiRobot.type(KeyCode.UP).sleep(200);
    }

    public void pressDownArrow() {
        guiRobot.type(KeyCode.DOWN).sleep(200);

    }

    public void pressControlZ_testUndoKeyCombo() {
//        guiRobot.push(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_ANY)).sleep(200);
        guiRobot.push(KeyCode.SHORTCUT, KeyCode.Z).sleep(200);
    }   

    public void pressControlShiftZ_testRedoKeyCombo() {
        guiRobot.push(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_ANY, KeyCombination.SHIFT_ANY))
                .sleep(200);
    }

}
