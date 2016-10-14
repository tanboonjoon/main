package seedu.address.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskForce taskForce;
    private final FilteredList<Task> filteredTasks;
    private final Deque<ReadOnlyTaskForce> undoTaskForceHistory = new LinkedList<ReadOnlyTaskForce>();
    private final Deque<ReadOnlyTaskForce> redoTaskForceHistory = new LinkedList<ReadOnlyTaskForce>();

    /**
     * Initializes a ModelManager with the given TaskForce
     * TaskForce and its variables should not be null
     */
    public ModelManager(TaskForce src, UserPrefs userPrefs) {
        
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskForce = new TaskForce(src);
        filteredTasks = new FilteredList<>(taskForce.getTasks());
//        taskHistory = new LinkedList<Pair<String, ArrayList<Task>>>();
//        taskHistory = new LinkedList<LinkedHashMap<String,ArrayList<ReadOnlyTask>>>();
//        undoHistory = new LinkedList<LinkedHashMap<String,ArrayList<ReadOnlyTask>>>();

    }

    public ModelManager() {
        this(new TaskForce(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskForce initialData, UserPrefs userPrefs) {
        taskForce = new TaskForce(initialData);
        filteredTasks = new FilteredList<>(taskForce.getTasks());
//        taskHistory = new LinkedList<Pair<String, ArrayList<Task>>>();
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

    /** Raises an event to indicate the model has changed */
    private void indicateTaskForceChanged() {
    	raiseEvent(new TaskForceChangedEvent(taskForce));
    }
    
    @Override
    public void raiseEvent(BaseEvent event) {
    	raise(event) ;
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        
        recordTaskForce(taskForce);
        taskForce.removeTask(target);
        indicateTaskForceChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        recordTaskForce(taskForce);
        taskForce.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskForceChanged();
    }
    
    //=========== Undo Task History Accessors ===============================================================
//
//	@Override
//	public void recordTask(String COMMAND_WORD, ArrayList<Task> taskList) {
//		taskHistory.push(new Pair<String, ArrayList<Task>>(COMMAND_WORD, taskList));		
//	}
//
//	@Override
//	public Pair<String, ArrayList<Task>> getPreviousTask() {
//		return taskHistory.pop();
//	}


    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName(), keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    @Override
    public synchronized boolean revertTaskForce() {
        
        if(undoTaskForceHistory.peekFirst() != null) {
            
            ReadOnlyTaskForce item = undoTaskForceHistory.pollFirst();
            redoTaskForceHistory.offerFirst(new TaskForce(taskForce));
            this.taskForce.resetData(item);
            indicateTaskForceChanged();
        }else{
            return false;
        }

        
        return true;
    }

    @Override
    public void recordTaskForce(ReadOnlyTaskForce taskForce) {
        undoTaskForceHistory.addFirst(new TaskForce(taskForce));
        if(undoTaskForceHistory.size() > 10 ) {
            undoTaskForceHistory.removeLast();
        }
        
        redoTaskForceHistory.clear();
        
    }
    @Override
    public synchronized boolean restoreTaskForce() {
          if(redoTaskForceHistory.peekFirst() != null) {
              ReadOnlyTaskForce item = redoTaskForceHistory.pollFirst();
              undoTaskForceHistory.offerFirst(new TaskForce(taskForce));
              this.taskForce.resetData(item);
              indicateTaskForceChanged();
          }else{
              return false;
          }
      
      return true;
    }
    
    @Override
    public void updateTask(ReadOnlyTask from, Task to) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException{
        recordTaskForce(taskForce);
        taskForce.removeTask(from);
        taskForce.addTask(to);
    }

}
