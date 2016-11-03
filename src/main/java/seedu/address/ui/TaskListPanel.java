package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
import seedu.address.commons.events.model.TaskForceTaskListChangedEvent;
import seedu.address.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.address.logic.filters.EventQualifier;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.logic.filters.Qualifier;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart {
    
    private static final Qualifier filter = new EventQualifier(false) ;
    
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "TaskListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;
    private List<ReadOnlyTask> taskList ;

    @FXML
    private ListView<ReadOnlyTask> taskListView;

    public TaskListPanel() {
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

    public static TaskListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
            ObservableList<ReadOnlyTask> taskList) {
        TaskListPanel taskListPanel = UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
        taskListPanel.configure(taskList);
        return taskListPanel;
    }

    private void configure(ObservableList<ReadOnlyTask> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        this.taskList = taskList ;
        
        Expression expression = new PredicateExpression(filter) ;
        taskListView.setItems(new FilteredList<ReadOnlyTask>(taskList, expression::satisfies ));
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();

        taskList.addListener(new ListChangeListener<ReadOnlyTask>() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                List<ReadOnlyTask> tasksAdded = Lists.newArrayList();
                List<ReadOnlyTask> tasksDeleted = Lists.newArrayList();

                while (change.next()) {
                    tasksAdded.addAll(change.getAddedSubList());
                    tasksDeleted.addAll(change.getRemoved());
                }

                raise(new TaskForceTaskListChangedEvent(tasksAdded, tasksDeleted));
            }
        });
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    public void scrollToTask(ReadOnlyTask task) {
        Platform.runLater(() -> {

            int index = taskListView.getItems().indexOf(task);

            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        public TaskListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                TaskCard newCard = TaskCard.load(task, taskList.indexOf(task));
                setGraphic(newCard.getLayout());
            }
        }
    }

}
