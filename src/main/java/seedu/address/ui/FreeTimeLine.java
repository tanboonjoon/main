package seedu.address.ui;

import java.util.Collections;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.commons.util.FxViewUtil;

// @@author A0135768R
public class FreeTimeLine extends UiPart {
    
    public static final int MAX_TIME_BLOCKS = 48 ;
    
    private static final String FXML = "FreeTimeBar.fxml";
    
    private AnchorPane timelinepane;
    private AnchorPane placeHolderPane;
    
    @FXML
    private AnchorPane timeline ;
    
    public static FreeTimeLine load(Stage primaryStage, AnchorPane commandBoxPlaceholder) {
        FreeTimeLine freeTime = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new FreeTimeLine());
        freeTime.configure();
        freeTime.addToPlaceholder();
        return freeTime;
    }
    
    public void configure() {
        registerAsAnEventHandler(this);
    }
    
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(timeline);
        FxViewUtil.applyAnchorBoundaryParameters(timelinepane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(timeline, 20.0, 0.0, 40.0, 0.0);
        
        timeline.setVisible(false);
    }

    @Override
    public void setNode(Node node) {
        timelinepane = (AnchorPane) node ;
        
    }

    @Override
    public String getFxmlPath() {
        return FXML ;
    }
    
    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }
    
    private void drawTimeline(List<Boolean> time) {
       for (int i =0 ; i < MAX_TIME_BLOCKS; i ++) {
           Rectangle rect = new Rectangle(15, 20, Paint.valueOf("blue")) ;
           
           rect.setLayoutX(5 + (15 * i));
           rect.setLayoutY(0);
           rect.setStroke(Paint.valueOf("black"));
           rect.setStrokeWidth(1F);
           
           timeline.getChildren().add(rect) ;
       }
    }
    
    @Subscribe
    private void onCommandExecutedEvent (TaskForceCommandExecutedEvent event) {
        drawTimeline(Collections.emptyList());
        timeline.setVisible(true);
        
    }

}
