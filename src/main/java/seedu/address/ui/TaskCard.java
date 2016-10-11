package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

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
    private Label deadline;
    @FXML
    private Label startline;

    private ReadOnlyTask task;
    private int displayedIndex;
    private DateTimeFormatter formatter;
    public TaskCard(){

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex){
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
    	deadline.setVisible(false);
    	startline.setVisible(false);
    	formatter = DateTimeFormatter.ofPattern("d MMM HHmm");
        name.setText(task.getName());
        id.setText(displayedIndex + ". ");
        description.setText(task.getDescription());
        tags.setText(task.tagsString());
        if(task instanceof Deadline) {
        	deadline.setVisible(true);
        	deadline.setText( ((Deadline)task).getEndDate().format(formatter).toString());
        }
        
        if(task instanceof Event) {
        	deadline.setVisible(true);
        	startline.setVisible(true);
        	deadline.setText( ((Event)task).getEndDate().format(formatter).toString());
        	startline.setText( ((Event)task).getStartDate().format(formatter).toString() + "  - ");

        }
        circle.getStyleClass().remove("circle_low") ;
        circle.getStyleClass().add("circle_high") ;
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
}
