package seedu.address.commons.events.model;

import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.task.ReadOnlyTask;

public class TaskForceTaskListChangedEvent extends BaseEvent {

    public final List<ReadOnlyTask> tasksAdded;
    public final List<ReadOnlyTask> tasksDeleted;

    public TaskForceTaskListChangedEvent(List<ReadOnlyTask> tasksAdded, List<ReadOnlyTask> tasksDeleted) {
        this.tasksAdded = tasksAdded;
        this.tasksDeleted = tasksDeleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (ReadOnlyTask task : tasksAdded) {
            sb.append(task.getName());
            sb.append(", ");
        }

        String resultingString;

        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());

            resultingString = "Names of tasks added: " + sb.toString();
        } else {
            resultingString = "Tasks deleted:" + tasksDeleted.size();
        }
        return resultingString;
    }

}
