package seedu.address.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.NameQualifier;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    
    public static final int MAX_UNDOS_REDOS = 10 ;

    private final TaskForce taskForce;
    private final FilteredList<Task> filteredTasks;
    private final Deque<ReadOnlyTaskForce> undoTaskForceHistory = new LinkedList<ReadOnlyTaskForce>();
    private final Deque<ReadOnlyTaskForce> redoTaskForceHistory = new LinkedList<ReadOnlyTaskForce>();

    /**
     * Initializes a ModelManager with the given TaskForce TaskForce and its
     * variables should not be null
     */
    public ModelManager(TaskForce src, UserPrefs userPrefs) {

        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskForce = new TaskForce(src);
        filteredTasks = new FilteredList<>(taskForce.getTasks());
    }

    public ModelManager() {
        this(new TaskForce(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskForce initialData, UserPrefs userPrefs) {
        taskForce = new TaskForce(initialData);
        filteredTasks = new FilteredList<>(taskForce.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskForce newData) {
        taskForce.resetData(newData);
        indicateTaskForceChanged();
    }

    @Override
    public ReadOnlyTaskForce getTaskForce() {
        return taskForce;
    }
    
    @Override
    public int getNextTaskId() {
        return taskForce.getNextTagId() ;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskForceChanged() {
        raiseEvent(new TaskForceChangedEvent(taskForce));
    }

    @Override
    public void raiseEvent(BaseEvent event) {
        raise(event);
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskForce.removeTask(target);
        indicateTaskForceChanged();
    }
    
    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskForce.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskForceChanged();
    }
    
    
    @Override
    public synchronized boolean revertTaskForce() {

        if (undoTaskForceHistory.peekFirst() != null) {

            ReadOnlyTaskForce item = undoTaskForceHistory.pollFirst();
            redoTaskForceHistory.offerFirst(new TaskForce(taskForce));
            this.taskForce.resetData(item);
            indicateTaskForceChanged();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void recordTaskForce() {
        undoTaskForceHistory.addFirst(new TaskForce(taskForce));
        if (undoTaskForceHistory.size() > MAX_UNDOS_REDOS) {
            undoTaskForceHistory.removeLast();
        }

        redoTaskForceHistory.clear();

    }

    @Override
    public synchronized boolean restoreTaskForce() {
        if (redoTaskForceHistory.peekFirst() != null) {
            ReadOnlyTaskForce item = redoTaskForceHistory.removeFirst();
            undoTaskForceHistory.offerFirst(new TaskForce(taskForce));
            this.taskForce.resetData(item);
            indicateTaskForceChanged();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void updateTask(ReadOnlyTask from, Task to)
            throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
        recordTaskForce();
        this.taskForce.removeTask(from);
        this.taskForce.addTask(to);
        indicateTaskForceChanged();
    }

    // =========== Filtered Task List Accessors
    // ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords, String findType) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords, findType)));
    }
    
    @Override
    public void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }


}

