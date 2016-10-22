package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.model.task.Block;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM h:mm a");

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
    		isDone.setVisible(true);
    		FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_DONE) ;
    		FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_DONE) ;
    	}
        
    	setTaskTitle();
        id.setText(displayedIndex + ".");
        
        setDescriptionText();
        
        displayTagString();
        
        displayStartAndEndDates();
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
    
    private void setDefaultStyle () {
        startline.setVisible(false);
        clock.setVisible(false);
        isDone.setVisible(false);
        
        circle.getStyleClass().add("circle_med") ;
    }
    
    private void setTaskTitle() {
        name.setText(task.getName());
        
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task ;
            
            if (deadline.isDeadlineOverdue()) {
                FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_OVERDUE) ;
                FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_HIGH) ;
            }
        }
        
        if (task instanceof Block) {
            FxViewUtil.setNodeStyle(name, NodeStyle.TITLE_BLOCK) ;
            FxViewUtil.setNodeStyle(circle, NodeStyle.CIRCLE_BLOCK) ;
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
            startline.setText( ((Deadline) task).getEndDate().format(FORMATTER).toString());
        }
        
        if(task instanceof Event) {
            startline.setVisible(true);
            clock.setVisible(true);
            
            String text = ((Event) task).getStartDate().format(FORMATTER).toString() + " to " + ((Event) task).getEndDate().format(FORMATTER).toString() ;
            startline.setText(text);

        }
    }
    
    private void displayTagString() {
        String tagString = task.tagsString() ;
        
        tags.setText(tagString);
        
        if (tagString.length() == 0) {
            tags.setVisible(false);
        }
    }
    
}
