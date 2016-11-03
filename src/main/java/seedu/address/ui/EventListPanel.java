package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.address.logic.filters.EventQualifier;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.logic.filters.Qualifier;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

public class EventListPanel extends UiPart {
    
    private static final Qualifier filter = new EventQualifier(true) ;
    
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);
    private static final String FXML = "EventListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;
    private List<ReadOnlyTask> taskList ;
    
    @FXML
    private ListView<ReadOnlyTask> eventListView;
    
    public EventListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static EventListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
            ObservableList<ReadOnlyTask> taskList) {
        EventListPanel eventListPanel = UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new EventListPanel());
        eventListPanel.configure(taskList);
        return eventListPanel;
    }

    private void configure(ObservableList<ReadOnlyTask> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }
    
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        this.taskList = taskList ;
        
        Expression expression = new PredicateExpression(filter) ;
        eventListView.setItems(new FilteredList<ReadOnlyTask>(taskList, expression::satisfies ));
        eventListView.setCellFactory(listView -> new EventListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }
    
    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }
    
    class EventListViewCell extends ListCell<ReadOnlyTask> {

        public EventListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);
            
            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {                
                TaskCard newCard = TaskCard.load(task, taskList.indexOf(task) + 1);
                setGraphic(newCard.getLayout());
            }
        }
    }

}
