package seedu.address.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskForce taskForce;
    private final FilteredList<Task> filteredTasks;

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
        taskForce.removeTask(target);
        indicateTaskForceChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskForce.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskForceChanged();
    }

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
    public void updateFilteredTaskList(Set<String> keywords, String findType){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords, findType)));
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
        private String findType;
        private String formattedDateForCompare;
        private String formattedTaskDate;
        private final int SAME_DAY_INDEX = 0;
        NameQualifier(Set<String> nameKeyWords, String findType) {
            this.nameKeyWords = nameKeyWords;
            this.findType = findType;
        }

        @Override
        public boolean run(ReadOnlyTask task) {

        	if(findType.equals("ALL")) {
                return nameKeyWords.stream()
                        .filter(keyword ->                     
                        StringUtil.containsIgnoreCase(task.getName(), keyword))
                        .findAny()
                        .isPresent();
        	}
        	
        	LocalDateTime dateToday = LocalDateTime.now();
        	DateTimeFormatter format_exclude_time = DateTimeFormatter.ofPattern("ddMMyyyy");
        	LocalDateTime dateForCompare = dateToday;
        	List<String> getTimeList = new ArrayList(nameKeyWords);
        	Long timeToAdd = Long.parseLong(getTimeList.get(0));
        	System.out.println("timeToAdd :" + timeToAdd);
        	if(findType.equals("WEEK")) {
        		dateForCompare = dateToday.plusWeeks(timeToAdd); 

        	}else if(findType.equals("DAY")) {
            	System.out.println("hey this is a day");
        		dateForCompare = dateToday.plusDays(timeToAdd);
        	}
        	
        	formattedDateForCompare = dateForCompare.format(format_exclude_time);
        	
        	if (task instanceof Deadline ) {
        		LocalDateTime taskDate = ((Deadline)task).getEndDate();
        		formattedTaskDate = taskDate.format(format_exclude_time);
        		System.out.println(formattedTaskDate);
        		System.out.println(formattedDateForCompare);
        		return formattedDateForCompare.compareTo(formattedTaskDate) == SAME_DAY_INDEX ;
        	}
        	if (task instanceof Event) {
        		LocalDateTime taskDate = ((Event)task).getEndDate();
        		formattedTaskDate = taskDate.format(format_exclude_time);
        		return formattedDateForCompare.compareTo(formattedTaskDate) == SAME_DAY_INDEX ;
        	}
        	
        	return true;
 
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
