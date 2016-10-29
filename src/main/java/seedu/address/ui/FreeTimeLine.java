package seedu.address.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.commands.FreetimeCommand;
import seedu.address.logic.commands.FreetimeCommand.TimeStatus;

// @@author A0135768R
public class FreeTimeLine extends UiPart {
    
    public static final int MAX_TIME_BLOCKS = 48 ;
    
    private static final String FXML = "FreetimeBar.fxml";
    
    private AnchorPane timelinepane;
    private AnchorPane placeHolderPane;
    
    private List<Rectangle> rectangles = Lists.newLinkedList() ;
    
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
    
    private void drawTimeline(List<TimeStatus> time) {
        
        assert time.size() == 24 || time.size() == 48 ;
        
        removeAllPreviousRects(timeline, rectangles) ;
        
        for (int i =0 ; i < MAX_TIME_BLOCKS; i ++) {
            Rectangle rect = new Rectangle(15, 20) ;

            rect.setLayoutX(5 + (15 * i));
            rect.setLayoutY(0);
            rect.setStroke(Paint.valueOf("black"));
            rect.setStrokeWidth(1F);
            rect.setOpacity(1);

            timeline.getChildren().add(rect) ;
            rectangles.add(rect) ;
            
            Color finalColor = determineRectColour((time.size() == 24) ? time.get(i/2) : time.get(i)) ;

            FillTransition ft = new FillTransition (Duration.millis(100 + (i*100)), rect, Color.TRANSPARENT, finalColor);

            ft.play();
        }
    }
    
    private static void removeAllPreviousRects (AnchorPane parent, Collection<Rectangle> listOfRect) {
        parent.getChildren().removeAll(listOfRect) ;
        listOfRect.clear();
    }
    
    private static Color determineRectColour (TimeStatus status) {
        if (status == TimeStatus.FREE) {
            return Color.web("#1aa260") ;
        }
        
        if (status == TimeStatus.NOT_FREE) {
            return Color.web("#de5347") ;
        }
        
        return Color.web("#7f7f7f") ;
        
    }
    
    @Subscribe
    private void onCommandExecutedEvent (TaskForceCommandExecutedEvent event) {
        if (event.commandInstance.getClass().equals(FreetimeCommand.class) && event.result.isSuccessfulCommand()) {
            
            FreetimeCommand command = (FreetimeCommand) event.commandInstance ;
            
            timeline.setVisible(true);
            drawTimeline(command.getFreeTimeLine()) ;
            
            
        } else {
            timeline.setVisible(false);
        }
        
    }

}
