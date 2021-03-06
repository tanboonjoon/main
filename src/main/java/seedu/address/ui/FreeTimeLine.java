package seedu.address.ui;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import javafx.util.Pair;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.commands.FreetimeCommand;
import seedu.address.logic.commands.FreetimeCommand.TimeStatus;

// @@author A0135768R
/**
 * The main controller for the freetime command result visuals
 *
 */
public class FreeTimeLine extends UiPart {

    public static final int MAX_TIME_BLOCKS = 48;

    private static final String FXML = "FreetimeBar.fxml";
    
    private static final int RECT_WIDTH = 15 ;
    private static final int RECT_HEIGHT = 25 ;
    private static final int STARTING_POS = 5 ;

    private AnchorPane timelinepane;
    private AnchorPane placeHolderPane;

    private List<Rectangle> rectangles = Lists.newLinkedList();

    @FXML
    private AnchorPane timeline;

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
        FxViewUtil.applyAnchorBoundaryParameters(timeline, 200.0, 0.0, 40.0, 0.0);

        timeline.setVisible(false);
    }

    @Override
    public void setNode(Node node) {
        timelinepane = (AnchorPane) node;

    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    private void drawTimeline(Map<Pair<Integer, Integer>, TimeStatus> timeStatuses) {

        TreeMap<Pair<Integer, Integer>, TimeStatus> sortedMap = sortTimeStatuses(timeStatuses);
        removeAllPreviousRects(timeline, rectangles);

        Entry<Pair<Integer, Integer>, TimeStatus> thisEntry = sortedMap.pollFirstEntry();

        int blockNo = 0;

        while (blockNo < MAX_TIME_BLOCKS) {

            while (thisEntry != null && blockNo >= thisEntry.getKey().getValue()) {
                thisEntry = sortedMap.pollFirstEntry();
            }

            Rectangle rect = createNewRect(blockNo);

            timeline.getChildren().add(rect);
            rectangles.add(rect);

            Color finalColor = null;

            if (thisEntry != null && blockNo >= thisEntry.getKey().getKey()
                    && blockNo < thisEntry.getKey().getValue()) {
                finalColor = determineRectColour(thisEntry.getValue());

            } else {
                finalColor = determineRectColour(null);
            }

            FillTransition ft = new FillTransition(Duration.millis(100 + (blockNo * 100)), rect, Color.TRANSPARENT,
                    finalColor);

            ft.play();

            blockNo++;

        }
    }

    private static void removeAllPreviousRects(AnchorPane parent, Collection<Rectangle> listOfRect) {
        parent.getChildren().removeAll(listOfRect);
        listOfRect.clear();
    }

    private static Rectangle createNewRect(int i) {

        Rectangle rect = new Rectangle(RECT_WIDTH, RECT_HEIGHT);

        rect.setLayoutX(STARTING_POS + (RECT_WIDTH * i));
        rect.setLayoutY(0);
        rect.setStroke(Paint.valueOf("black"));
        rect.setStrokeWidth(1F);
        rect.setOpacity(1);

        return rect;
    }

    private static TreeMap<Pair<Integer, Integer>, TimeStatus> sortTimeStatuses(
            Map<Pair<Integer, Integer>, TimeStatus> statusMap) {

        TreeMap<Pair<Integer, Integer>, TimeStatus> treeMap = new TreeMap<>(new Comparator<Pair<Integer, Integer>>() {

            @Override
            public int compare(Pair<Integer, Integer> arg0, Pair<Integer, Integer> arg1) {
                if (arg0.getKey() < arg1.getKey()) {
                    return -1;
                }

                if (arg0.getKey() > arg1.getKey()) {
                    return 1;
                }

                return 0;
            }

        });

        treeMap.putAll(statusMap);

        return treeMap;

    }

    private static Color determineRectColour(TimeStatus status) {
        if (status == TimeStatus.FREE) {
            return Color.web("#1aa260");
        }

        if (status == TimeStatus.NOT_FREE) {
            return Color.web("#de5347");
        }

        return Color.web("#7f7f7f");

    }

    @Subscribe
    private void onCommandExecutedEvent(TaskForceCommandExecutedEvent event) {
        if (event.commandInstance.getClass().equals(FreetimeCommand.class) && event.result.isSuccessfulCommand()) {

            FreetimeCommand command = (FreetimeCommand) event.commandInstance;

            timeline.setVisible(true);

            drawTimeline(command.getFreeTimeLine());

        } else {
            timeline.setVisible(false);
        }

    }

}
