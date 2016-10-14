package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.model.task.Task;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList;

import java.util.ArrayList;
import java.util.Set;

import javafx.util.Pair;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskForce newData);

    /** Returns the TaskForce */
    ReadOnlyTaskForce getTaskForce();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all task */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Raises the given event to the event handler **/
    void raiseEvent(BaseEvent event) ;
    
	/* a method to store a new task into the task history */
	public void recordTask(String COMMAND_WORD, ArrayList<Task> taskList);
	
	/* a method to pop out the latest task from history so as to update it */
	public Pair<String, ArrayList<Task>> getPreviousTask();

}
