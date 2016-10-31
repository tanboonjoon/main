package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskForce;

/** Indicates the TaskForce in the model has changed */
public class TaskForceChangedEvent extends BaseEvent {

    public final ReadOnlyTaskForce data;

    public TaskForceChangedEvent(ReadOnlyTaskForce data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
