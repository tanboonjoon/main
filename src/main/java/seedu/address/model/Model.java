package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.filters.Expression;
import seedu.address.model.task.Task;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList;
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
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all task */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords, String findType);
    
    /** Raises the given event to the event handler **/
    void raiseEvent(BaseEvent event) ;
    
    /** Revert changes for undo command */
    BaseEvent revertChanges();
    
    /** Restore changes for redo command */
    BaseEvent restoreChanges();
        
    /** Gets the next available Task ID */
    public int getNextTaskId() ;
    
    /** 
     * Filters the task list with the given expression. This function will not affect the list
     * currently shown to the user
     */
    public void searchTaskList(Expression expression) ;
    
    public UnmodifiableObservableList<ReadOnlyTask> getSearchedTaskList() ;

}
