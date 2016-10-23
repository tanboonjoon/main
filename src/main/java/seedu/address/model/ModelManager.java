package seedu.address.model;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
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
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
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
    
    private static final int TASK_LESS_THAN_DEADLINE = -1;
    private static final int TASK_LESS_THAN_EVENT = -2;
    private static final int DEADLINE_MORE_THAN_TASK = 1;
    private static final int DEADLINE_LESS_THAN_EVENT = -1;
    private static final int EVENT_MORE_THAN_TASK = 2;
    private static final int EVENT_MORE_THAN_DEADLINE = 1;
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
        ObservableList<Task> taskList = taskForce.getTasks();
        sortFilteredList(taskList);
        filteredTasks = new FilteredList<>(taskList);
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
       // sortFilteredList(taskForce.getTasks());
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
    // ===============================================================
    // ======================= Comparators  ==========================
    // ===============================================================
    
    //@@A0139942W
    /*
     * To sort the observableList before it is attached to a unmodifiable FilterList
     * return a sortedLists sorted by type, follow by dates
     * The ranking of class is as followed, Task < Deadline < Event
     */
    public void sortFilteredList(ObservableList<Task> taskList) {
    	taskList.sort(new Comparator<Task>() {
    		@Override
    		public int compare(Task task1, Task task2) {
    			if (task1 instanceof Event) {
    				return sortByEvent( (Event)task1, task2);
    			}

    			if (task1 instanceof Deadline) {
    				return sortByDeadline( (Deadline)task1, task2);
    			}
    			return sortByTask(task1, task2);

    		}});
    }

    public int sortByTask(Task task1, Task task2) {
    	if (task2 instanceof Deadline) {
    		return TASK_LESS_THAN_DEADLINE;
    	}
    	if (task2 instanceof Event) {
    		return TASK_LESS_THAN_EVENT;
    	}
    	return task1.getName().compareTo(task2.getName());
    }

    public int sortByDeadline(Deadline deadline, Task task) {
    	if (task instanceof Deadline) {
    		Deadline deadline2 = (Deadline) task;
    		return deadline.getEndDate().compareTo(deadline2.getEndDate());
    	}

    	if (task instanceof Event) {
    		return  DEADLINE_LESS_THAN_EVENT;
    	}

    	return  DEADLINE_MORE_THAN_TASK;
    }

    public int sortByEvent(Event event, Task task) {
    	if (task instanceof Event) {
    		Event event2 = (Event) task;
    		return event.getStartDate().compareTo(event2.getStartDate());
    	}

    	if (task instanceof Deadline) {
    		return EVENT_MORE_THAN_DEADLINE;
    	}
    	return EVENT_MORE_THAN_TASK;
    }



}

