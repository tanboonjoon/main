package seedu.address.logic.filters;

import seedu.address.model.task.ReadOnlyTask;

/**
 * @@author A0135768R
 * 
 * A simple qualifier to filter tasks via their task ID.
 *
 */
public class TaskIdentifierNumberQualifier implements Qualifier {
    
    private final int filterId ;
    
    public TaskIdentifierNumberQualifier (int idToFilter) {
        this.filterId = idToFilter ;
    }

    @Override
    public boolean run(ReadOnlyTask task) {
        
        return task != null && task.getTaskId() == filterId ;
    }

}
