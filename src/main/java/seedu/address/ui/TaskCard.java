package seedu.address.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import seedu.address.commons.util.DateUtil;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.model.task.Block;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

// @@author A0135768R
/**
 * The individual card element in GUI to display details of each task
 *
 */
public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM h:mm a");

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label tags;
    @FXML
    private Circle circle;
    @FXML
    private Label startline;

    @FXML
    private Rectangle descBar ;
    @FXML
    private ImageView clock ;
    @FXML
    private ImageView isDone;

    private ReadOnlyTask task;
    private int displayedIndex;

    public static TaskCard load(ReadOnlyTask task, int displayedIndex){
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        setDefaultStyle() ;

        if (task.getDoneStatus()) {
            setTaskCardToDone() ;
        }

        setTaskTitle();
        displayTaskId() ;
        setDescriptionText() ;
        displayTagString() ;
        displayStartAndEndDates() ;
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    private void setTaskCardToDone() {
        isDone.setVisible(true);
        FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_DONE) ;
        FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_DONE) ;
        FxViewUtil.setNodeStyle(startline, NodeStyle.TIME_DONE) ;
    }
    
    private void displayTaskId() {
        id.setText(displayedIndex + ".");
    }

    private void setDefaultStyle () {
        startline.setVisible(false);
        clock.setVisible(false);
        isDone.setVisible(false);

        circle.getStyleClass().add("circle_med") ;
    }

    private void setTaskTitle() {
        name.setText(task.getName());

        if (task instanceof Deadline && ((Deadline) task).isDeadlineOverdue()) {
            FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_OVERDUE) ;
            FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_HIGH) ;
            FxViewUtil.setNodeStyle(startline, NodeStyle.TIME_OVERDUE) ;
        }

        if (task instanceof Block) {
            FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_BLOCK) ;
            FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_BLOCK) ;
            FxViewUtil.setNodeStyle(startline, NodeStyle.TIME_BLOCK) ;
        }
    }

    private void setDescriptionText () {
        description.setText(task.getDescription());

        if (task.getDescription().length() == 0) {
            descBar.setVisible(false);
        }
    }

    private void displayStartAndEndDates() {
        if(task instanceof Deadline) {
            clock.setVisible(true);
            startline.setVisible(true);

            LocalDateTime deadline = ((Deadline) task).getEndDate() ;

            long difference = DateUtil.getTimeDifferenceFromNow(deadline, ChronoUnit.SECONDS) ;

            if (Math.abs(difference) > 5 * 3600) {
                // If task is more than 5 hours from now, display the absolute dates 
                startline.setText( deadline.format(FORMATTER).toString());

            } else {
                FxViewUtil.setNodeStyle(startline, (difference > 0) ? NodeStyle.TIME_UPCOMING : null) ;
                startline.setText(DateUtil.getRelativeDateFromNow(deadline));
            }
        }

        if(task instanceof Event) {
            startline.setVisible(true);
            clock.setVisible(true);

            String dateTimeText = formatEventDateString( ((Event) task).getStartDate(), ((Event) task).getEndDate()) ;
            startline.setText(dateTimeText);

        }
    }

    private void displayTagString() {
        String tagString = task.tagsString() ;

        tags.setText(tagString);

        if (tagString.length() == 0) {
            tags.setVisible(false);
        }
    }
    
    private String formatEventDateString(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        StringBuilder buffer = new StringBuilder() ;
        
        buffer.append(startDateTime.format(FORMATTER)) ;
        buffer.append(" to ") ;
        buffer.append(endDateTime.format(FORMATTER)) ;
        
        return buffer.toString() ;
    }

}
