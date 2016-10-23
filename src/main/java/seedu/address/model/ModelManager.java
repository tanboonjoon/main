package seedu.address.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.logic.filters.AlwaysTrueQualifier;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.NameQualifier;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
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
    private final FilteredList<Task> filteredTasksForSearching;
    private final Deque<TaskForceCommandExecutedEvent> undoTaskForceHistory = new LinkedList<TaskForceCommandExecutedEvent>();
    private final Deque<TaskForceCommandExecutedEvent> redoTaskForceHistory = new LinkedList<TaskForceCommandExecutedEvent>();

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
        filteredTasksForSearching = new FilteredList<>(taskForce.getTasks());
    }

    public ModelManager() {
        this(new TaskForce(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskForce initialData, UserPrefs userPrefs) {
        taskForce = new TaskForce(initialData);
        filteredTasks = new FilteredList<>(taskForce.getTasks());
        filteredTasksForSearching = new FilteredList<>(taskForce.getTasks());
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
        // updateFilteredListToShowAll();
        indicateTaskForceChanged();
    }
    
    
    @Override
    public TaskForceCommandExecutedEvent revertChanges() {

        if (undoTaskForceHistory.peekFirst() != null) {

            return  undoTaskForceHistory.pollFirst();
            
        } 
        return null;   
    }


    @Override
    public TaskForceCommandExecutedEvent restoreChanges() {
        if (redoTaskForceHistory.peekFirst() != null) {
            return redoTaskForceHistory.removeFirst();
        }
        return null;
    }

    
    public void saveChanges(Deque<TaskForceCommandExecutedEvent> history,TaskForceCommandExecutedEvent event, int size){
        history.push(event);
        if(history.size() > size){
            history.removeLast();
        }
    }
    
    
    private void saveCommandChanges(TaskForceCommandExecutedEvent event) {
        if(!(event.commandInstance.getCommandChanges().getKey().isEmpty() && event.commandInstance.getCommandChanges().getValue().isEmpty())){
            if(event.commandInstance.getClass().getSimpleName().equals("RedoCommand")) {
                saveChanges(undoTaskForceHistory, event, MAX_UNDOS_REDOS);
            }else if(event.commandInstance.getClass().getSimpleName().equals("UndoCommand")){
                saveChanges(redoTaskForceHistory, event, MAX_UNDOS_REDOS);
            }else{
                saveChanges(undoTaskForceHistory, event, MAX_UNDOS_REDOS);
                redoTaskForceHistory.clear();
            } 
        }
    }
    // ===============================================================
    // =========== Filtered Task List Accessors ======================
    // ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        updateFilteredTaskList(new PredicateExpression(new AlwaysTrueQualifier()));
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords, String findType) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords, findType)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    @Override
    public void searchTaskList(Expression expression) {
        filteredTasksForSearching.setPredicate(expression::satisfies);
    }
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getSearchedTaskList() {
        return new UnmodifiableObservableList<>(filteredTasksForSearching);
    }
    
    // ===============================================================
    // ======================= Event Listeners =======================
    // ===============================================================
   
    @Subscribe
    public void onCommandExecutedEvent (TaskForceCommandExecutedEvent event) {
        if (event.result.isSuccessfulCommand()) {
            saveCommandChanges(event);
        }
    }
}

