package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.filters.Expression;
import seedu.address.model.task.Task;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList;

import java.util.List;
import java.util.Set;

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

     /** Update the given task */
    void updateTask(ReadOnlyTask from, Task to) throws UniqueTaskList.TaskNotFoundException ,UniqueTaskList.DuplicateTaskException;
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all task */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords, String findType);
    
    /** Raises the given event to the event handler **/
    void raiseEvent(BaseEvent event) ;
    
    /** Revert Task Force to previous state by one step. Use in conjunction with recordTaskForce. */
    boolean revertTaskForce();
    
    /** restore Task Force by one step. Use in conjunction with recordTaskForce */
    boolean restoreTaskForce();
    
    /* a method to store a new task into the task history */
    public void recordTaskForce();
    
    /** Gets the next available Task ID */
    public int getNextTaskId() ;
	
    /** Filters the task list with the given expression */
    public void updateFilteredTaskList(Expression expression) ;

}
