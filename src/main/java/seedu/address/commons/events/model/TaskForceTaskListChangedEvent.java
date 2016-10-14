package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

public class TaskForceTaskListChangedEvent extends BaseEvent {
    
    public final int targetIndex ;
    
    public TaskForceTaskListChangedEvent (int index) {
        this.targetIndex = index ;
    }
    
    @Override
    public String toString() {
        return "new index: " + targetIndex ;
    }

}
