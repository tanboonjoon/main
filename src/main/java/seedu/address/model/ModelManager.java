package seedu.address.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
	private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

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

	private void updateFilteredTaskList(Expression expression) {
		filteredTasks.setPredicate(expression::satisfies);
	}

	// ========== Inner classes/interfaces used for filtering
	// ==================================================

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

	// @@author A0139942W
	private class NameQualifier implements Qualifier {
		private Set<String> nameKeyWords;
		private String findType;

		private DateTimeFormatter format_exclude_time;
		private final boolean TASK_NOT_FOUND = false;
		private final boolean TASK_FOUND = true;
		private final int START_DAY_INDEX = 0;
		private final int LAST_DAY_INDEX = 7;
		private final int SAME_DAY_VALUE = 0;
		private final int DATE_ARGS_INDEX = 0;
		private final int FORMATTED_DATE_INDEX = 0;
		private final int GET_TO_MONDAY_INDEX = 1;


		private ArrayList<String> formattedDateList;
		private ArrayList<LocalDateTime> dateToCompareList;

		NameQualifier(Set<String> nameKeyWords, String findType) {
			initializeData();

			this.format_exclude_time = DateTimeFormatter.ofPattern("ddMMyyyy");
			this.nameKeyWords = nameKeyWords;
			this.findType = findType;
		}

		public void initializeData() {
			this.formattedDateList = new ArrayList<String>();
			this.dateToCompareList = new ArrayList<LocalDateTime>();
		}

		@Override
		public boolean run(ReadOnlyTask task) {

			if (findType.equals("ALL")) {
				return nameKeyWords.stream().filter(keyword -> StringUtil.containsIgnoreCase(task.getName(), keyword))
						.findAny().isPresent();
			}

			getDateForCompare();
			getFormattedDate();

			if (task instanceof Deadline) {
				String taskEndDate = (((Deadline) task).getEndDate()).format(format_exclude_time);
				return filterDeadLine(taskEndDate);
			}

			if (task instanceof Event) {
				String taskStartDate = ((Event) task).getStartDate().format(format_exclude_time);
				String taskEndDate = ((Event) task).getEndDate().format(format_exclude_time);
				return filterEvent(taskStartDate, taskEndDate);
			}

			return TASK_FOUND;

		}

		public boolean filterDeadLine(String taskStartDate) {

			if (findType.equals("DAY")) {
				return formattedDateList.get(FORMATTED_DATE_INDEX).compareTo(taskStartDate) == SAME_DAY_VALUE;
			}

			for (int day_index = 0; day_index < LAST_DAY_INDEX; day_index++) {
				if (formattedDateList.get(day_index).compareTo(taskStartDate) == SAME_DAY_VALUE) {
					return TASK_FOUND;
				}

			}

			return false;
		}

		public boolean filterEvent(String taskStartDate, String taskEndDate) {
			if (findType.equals("DAY")) {
				return formattedDateList.get(FORMATTED_DATE_INDEX).compareTo(taskStartDate) == SAME_DAY_VALUE
						|| formattedDateList.get(FORMATTED_DATE_INDEX).compareTo(taskEndDate) == SAME_DAY_VALUE;
			}

			for (int day_index = 0; day_index < LAST_DAY_INDEX; day_index++) {
				String formattedDate = formattedDateList.get(day_index);
				if (formattedDate.compareTo(taskStartDate) == SAME_DAY_VALUE) {
					return TASK_FOUND;
				}
				if (formattedDate.compareTo(taskEndDate) == SAME_DAY_VALUE) {
					return TASK_FOUND;
				}
			}

			return TASK_NOT_FOUND;
		}

		private void getFormattedDate() {
			for (int i = 0; i < dateToCompareList.size(); i ++) {
				formattedDateList.add(dateToCompareList.get(i).format(format_exclude_time));
			}


		}

		public void getDateForCompare() {
			LocalDateTime dateToday = LocalDateTime.now();
			LocalDateTime dateForCompare = dateToday;
			Long timeToAdd = parseTimeToLong(nameKeyWords);


			
			if (findType.equals("DAY")) {
				dateForCompare = dateToday.plusDays(timeToAdd);
				dateToCompareList.add(dateForCompare);
				return;
			}
			
			LocalDateTime dateOfThatWeek = dateToday.plusWeeks(timeToAdd);
			int dayOfThatWeek = dateOfThatWeek.getDayOfWeek().getValue();
			dateOfThatWeek = dateOfThatWeek.minusDays(dayOfThatWeek);
			dateOfThatWeek = dateOfThatWeek.plusDays(GET_TO_MONDAY_INDEX);
			
			for (int day_index = START_DAY_INDEX; day_index < LAST_DAY_INDEX; day_index++) {
				dateForCompare = dateOfThatWeek.plusDays(day_index);
				System.out.println(dateForCompare + " " + dateForCompare.getDayOfWeek());
				dateToCompareList.add(dateForCompare);
			}
			
			

		}

		public Long parseTimeToLong(Set<String> nameKeyWords) {
			List<String> getTimeList = new ArrayList<String>(nameKeyWords);
			return Long.parseLong(getTimeList.get(DATE_ARGS_INDEX));
		}
		// @@author A0139942W

		@Override
		public String toString() {
			return "name=" + String.join(", ", nameKeyWords);
		}
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
	public void recordTaskForce(ReadOnlyTaskForce taskForce) {
		undoTaskForceHistory.addFirst(new TaskForce(taskForce));
		if (undoTaskForceHistory.size() > 10) {
			undoTaskForceHistory.removeLast();
		}

		redoTaskForceHistory.clear();

	}

	@Override
	public synchronized boolean restoreTaskForce() {
		if (redoTaskForceHistory.peekFirst() != null) {
			ReadOnlyTaskForce item = redoTaskForceHistory.pollFirst();
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
		recordTaskForce(taskForce);
		this.taskForce.removeTask(from);
		this.taskForce.addTask(to);
		indicateTaskForceChanged();
	}

}
