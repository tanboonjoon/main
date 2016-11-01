package seedu.address.ui;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.address.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.*;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.commons.core.LogsCenter;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    private String previousCommandText;
    private List<String> commandHistory = Lists.newArrayList();
    private ListIterator<String> commandIterator = commandHistory.listIterator();
    private final KeyCombination undoKeyCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
    private final KeyCombination redoKeyCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN,
            KeyCombination.SHIFT_DOWN);

    private Logic logic;

    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder, ResultDisplay resultDisplay,
            Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

    public void configure(ResultDisplay resultDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
        this.logic = logic;
        commandTextField.getStyleClass().removeAll();
        commandTextField.getStyleClass().add("command-box");

        setKeyComboEvent(logic);

        registerAsAnEventHandler(this);
    }

    // @@author A0140037W
    /**
     * Attach Key event to command text field and set listener to vavrious key
     * events.
     */
    private void setKeyComboEvent(Logic logic) {
        commandTextField.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (undoKeyCombo.match(key)) {
                logic.execute(UndoCommand.COMMAND_WORD);
            } else if (redoKeyCombo.match(key)) {
                logic.execute(RedoCommand.COMMAND_WORD);
            } else if (key.getCode() == KeyCode.UP) {
                this.previousStoredCommand();
            } else if (key.getCode() == KeyCode.DOWN) {
                this.nextStoredCommand();
            }
        });
    }

    // @@author reused
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    @FXML
    private void handleCommandInputChanged() {
        // Take a copy of the command text
        saveCommandText();

        /*
         * We assume the command is correct. If it is incorrect, the command box
         * will be changed accordingly in the event handling code {@link
         * #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        mostRecentResult = logic.execute(previousCommandText);
        resultDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);
    }

    // @@author A0140037W
    /*
     * Save command Text into command history list.
     */
    private void saveCommandText() {
        previousCommandText = commandTextField.getText();
        commandHistory.add(0, previousCommandText);
        commandIterator = commandHistory.listIterator();

    }

    // @@author reused
    /**
     *  Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Invalid command: " + previousCommandText));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandText);
    }

    // @@author A0140037W
    /*
     * Iterate to next command text in the list.
     */
    private void nextStoredCommand() {
        if (commandIterator.hasPrevious()) {
            previousCommandText = commandIterator.previous();
            restoreCommandText();
        }
    }

    // @@author A0140037W
    /*
     * Iterate to previous command text in the list.
     */
    private void previousStoredCommand() {
        if (commandIterator.hasNext()) {
            previousCommandText = commandIterator.next();
            restoreCommandText();
        }
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }

}
