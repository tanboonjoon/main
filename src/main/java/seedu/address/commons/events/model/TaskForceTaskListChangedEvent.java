package seedu.address.commons.events.model;

import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.task.ReadOnlyTask;

public class TaskForceTaskListChangedEvent extends BaseEvent {
    
    public final List<ReadOnlyTask> tasks ;
    
    public TaskForceTaskListChangedEvent (List<ReadOnlyTask> tasks) {
        this.tasks = tasks; 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder () ;
        
        for (ReadOnlyTask task : tasks) {
            sb.append(task.getName()) ;
            sb.append(", ") ;
        }
        
        sb.delete(sb.length() - 2, sb.length());
        return "Names of tasks added: " + sb.toString() ;
    }

}
