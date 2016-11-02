package seedu.address.model;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.events.model.TaskForceCommandExecutedEvent;
import seedu.address.logic.filters.AlwaysTrueQualifier;
import seedu.address.logic.filters.Expression;
import seedu.address.logic.filters.NameQualifier;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.model.tag.ReadOnlyTagRegistrar;
import seedu.address.model.tag.TagRegistrar;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the Taskforce list data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    public static final int MAX_UNDOS_REDOS = 10;

    private final TaskForce taskForce;
    private final FilteredList<Task> filteredTasks;
    private final SortedList<Task> sortedFilteredTasks;
    private final FilteredList<Task> filteredTasksForSearching;
    private final Deque<TaskForceCommandExecutedEvent> undoTaskForceHistory = new LinkedList<TaskForceCommandExecutedEvent>();
    private final Deque<TaskForceCommandExecutedEvent> redoTaskForceHistory = new LinkedList<TaskForceCommandExecutedEvent>();
    private final TagRegistrar tagRegistrar = new TagRegistrar();
    private final Config config;
    
    private final FilteredList<Task> filteredTasksForTasksDisplay;
    private final FilteredList<Task> filteredTasksForEventsDisplay;

    private final SortedList<Task> sortedFilteredTasksForTasksDisplay;
    private final SortedList<Task> sortedFilteredTasksForEventsDisplay;
    

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
    public ModelManager(TaskForce src, Config config) {

        super();
        assert src != null;
        assert config != null;

        logger.fine("Initializing with address book: " + src);

        this.config = config;

        taskForce = new TaskForce(src);
        tagRegistrar.setAllTags(src.getTagList());
        filteredTasks = new FilteredList<>(taskForce.getTasks());
        sortedFilteredTasks = setUpSortedList();
        filteredTasksForSearching = new FilteredList<>(taskForce.getTasks());
        filteredTasksForTasksDisplay = setUpTasksOnlyList();
        filteredTasksForEventsDisplay = setUpEventsOnlyList();
        sortedFilteredTasksForTasksDisplay = setUpSortedTasksOnlyList();
        sortedFilteredTasksForEventsDisplay = setUpSortedEventsOnlyList();
    }

    public ModelManager() {
        this(new TaskForce(), new Config());
    }

    public ModelManager(ReadOnlyTaskForce initialData, Config config) {
        this.config = config;

        taskForce = new TaskForce(initialData);
        tagRegistrar.setAllTags(initialData.getTagList());
        filteredTasks = new FilteredList<>(taskForce.getTasks());
        sortedFilteredTasks = setUpSortedList();
        filteredTasksForSearching = new FilteredList<>(taskForce.getTasks());
        filteredTasksForTasksDisplay = setUpTasksOnlyList();
        filteredTasksForEventsDisplay = setUpEventsOnlyList();
        sortedFilteredTasksForTasksDisplay = setUpSortedTasksOnlyList();
        sortedFilteredTasksForEventsDisplay = setUpSortedEventsOnlyList();
    }

	@Override
    public void resetData(ReadOnlyTaskForce newData) {
        taskForce.resetData(newData);
        tagRegistrar.setAllTags(newData.getTagList());
        indicateTaskForceChanged();
        this.undoTaskForceHistory.clear();
        this.redoTaskForceHistory.clear();
    }

    @Override
    public ReadOnlyTaskForce getTaskForce() {
        return taskForce;
    }

    @Override
    public Config getConfigs() {
        return config;
    }

    @Override
    public ReadOnlyTagRegistrar getTagRegistry() {
        return tagRegistrar;
    }

    @Override
    public int getNextTaskId() {
        return taskForce.getNextTagId();
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

            return undoTaskForceHistory.pollFirst();

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

    public void saveChanges(Deque<TaskForceCommandExecutedEvent> history, TaskForceCommandExecutedEvent event,
            int size) {
        history.push(event);
        if (history.size() > size) {
            history.removeLast();
        }
    }

    private void saveCommandChanges(TaskForceCommandExecutedEvent event) {
        if (!(event.commandInstance.getCommandChanges().getKey().isEmpty()
                && event.commandInstance.getCommandChanges().getValue().isEmpty())) {
            if (event.commandInstance.getClass().getSimpleName().equals("RedoCommand")) {
                saveChanges(undoTaskForceHistory, event, MAX_UNDOS_REDOS);
            } else if (event.commandInstance.getClass().getSimpleName().equals("UndoCommand")) {
                saveChanges(redoTaskForceHistory, event, MAX_UNDOS_REDOS);
            } else {
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
        return getSortedFilteredTask();
    }

    @Override
    public void updateFilteredListToShowAll() {
        updateFilteredTaskList(new PredicateExpression(new AlwaysTrueQualifier()));
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords, String findType, boolean isCheckMark) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords, findType, isCheckMark)));
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
    public void onCommandExecutedEvent(TaskForceCommandExecutedEvent event) {
        if (event.result.isSuccessfulCommand()) {
            saveCommandChanges(event);
        }
    }
    // ===============================================================
    // ======================= Comparators ==========================
    // ===============================================================

    // @@A0139942W
    /*
     * Wrapping a SortedList around the FilteredList that wrap the
     * ObservableList return a sortedLists sorted by type, follow by dates The
     * ranking of class is as followed, Task < Deadline < Event
     */

    private SortedList<Task> setUpSortedList() {
        // TODO Auto-generated method stub
        SortedList<Task> sortedList = new SortedList<>(this.filteredTasks, (Task task1, Task task2) -> {
            if (task1 instanceof Event) {
                return sortByEvent((Event) task1, task2);
            }

            if (task1 instanceof Deadline) {
                return sortByDeadline((Deadline) task1, task2);
            }
            return sortByTask(task1, task2);
        });
        return sortedList;
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
            return DEADLINE_LESS_THAN_EVENT;
        }

        return DEADLINE_MORE_THAN_TASK;
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

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getSortedFilteredTask() {
        // TODO Auto-generated method stub
        return new UnmodifiableObservableList<>(sortedFilteredTasks);
    }

    // @@author: A0111277M
    /*
     * Allows the Taskforce App to start with today's tasks
     */

    public UnmodifiableObservableList<ReadOnlyTask> startWithTodaysTasks() {
        Set<String> keywordSet = new HashSet<String>();
        keywordSet.add("0");
        updateFilteredTaskList(keywordSet, "DAY", false);
        return new UnmodifiableObservableList<>(sortedFilteredTasksForTasksDisplay);
    }
    
    public UnmodifiableObservableList<ReadOnlyTask> startWithTodaysEvents() {
        Set<String> keywordSet = new HashSet<String>();
        keywordSet.add("0");
        updateFilteredTaskList(keywordSet, "DAY", false);
        return new UnmodifiableObservableList<>(sortedFilteredTasksForEventsDisplay);
    }

    private FilteredList<Task> setUpEventsOnlyList() {
		FilteredList<Task> eventsOnlyFilteredList = new FilteredList<Task>(this.filteredTasks, (ReadOnlyTask task) -> {
			return (task instanceof Event);
		});
		return eventsOnlyFilteredList;
	}

	private FilteredList<Task> setUpTasksOnlyList() {
		FilteredList<Task> tasksOnlyFilteredList = new FilteredList<Task>(this.filteredTasks, (ReadOnlyTask task) -> {
			return !(task instanceof Event);
		});
		return tasksOnlyFilteredList;
	}
	
    private SortedList<Task> setUpSortedTasksOnlyList() {
        // TODO Auto-generated method stub
        SortedList<Task> sortedList = new SortedList<>(this.filteredTasksForTasksDisplay, (Task task1, Task task2) -> {
            if (task1 instanceof Event) {
                return sortByEvent((Event) task1, task2);
            }

            if (task1 instanceof Deadline) {
                return sortByDeadline((Deadline) task1, task2);
            }
            return sortByTask(task1, task2);
        });
        return sortedList;
    }
    
    private SortedList<Task> setUpSortedEventsOnlyList() {
        // TODO Auto-generated method stub
        SortedList<Task> sortedList = new SortedList<>(this.filteredTasksForEventsDisplay, (Task task1, Task task2) -> {
            if (task1 instanceof Event) {
                return sortByEvent((Event) task1, task2);
            }

            if (task1 instanceof Deadline) {
                return sortByDeadline((Deadline) task1, task2);
            }
            return sortByTask(task1, task2);
        });
        return sortedList;
    }
}
