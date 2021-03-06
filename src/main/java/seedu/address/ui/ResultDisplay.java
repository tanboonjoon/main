package seedu.address.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart {
    public static final String RESULT_DISPLAY_ID = "resultDisplay";
    private static final String STATUS_BAR_STYLE_SHEET = "result-display";
    private TextArea resultDisplayArea;
    private final StringProperty displayed = new SimpleStringProperty("");

    private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static ResultDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        ResultDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ResultDisplay());
        statusBar.configure();
        return statusBar;
    }

    public void configure() {
        resultDisplayArea = new TextArea();
        resultDisplayArea.setEditable(false);
        resultDisplayArea.setId(RESULT_DISPLAY_ID);
        resultDisplayArea.getStyleClass().removeAll();
        resultDisplayArea.getStyleClass().add(STATUS_BAR_STYLE_SHEET);
        resultDisplayArea.setText("");
        resultDisplayArea.textProperty().bind(displayed);
        resultDisplayArea.setWrapText(true);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(resultDisplayArea);
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public void postMessage(String message) {
        displayed.setValue(message);
    }

    // @@author A0135768R
    public void commandExecuted(boolean success) {
        String addedClass;

        if (!success) {
            addedClass = "badCommand";
        } else {
            addedClass = "validCommand";
        }

        resultDisplayArea.getStyleClass().add(addedClass);

        Timeline reduceBorder = new Timeline(new KeyFrame(Duration.millis(2500), runnable -> reduceBorderWidth()));

        Timeline backToNormal = new Timeline(
                new KeyFrame(Duration.millis(5000), runnable -> changeBorderBack(addedClass)));

        reduceBorder.play();
        backToNormal.play();
    }

    private void changeBorderBack(String classToRemove) {
        resultDisplayArea.getStyleClass().remove(classToRemove);
    }

    private void reduceBorderWidth() {
        resultDisplayArea.setStyle("-fx-border-width: 2px ;");
    }

}
