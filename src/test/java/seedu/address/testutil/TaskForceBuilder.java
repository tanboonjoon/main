package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.TaskForce;

/**
 * A utility class to help with building TaskForce objects. Example usage: <br>
 * {@code TaskForce ab = new TaskForceBuilder().withTask("John", "Doe").withTag("Friend").build();}
 */
public class TaskForceBuilder {

    private TaskForce taskForce;

    public TaskForceBuilder(TaskForce taskForce) {
        this.taskForce = taskForce;
    }

    public TaskForceBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskForce.addTask(task);
        return this;
    }

    public TaskForceBuilder withTag(String tagName) throws IllegalValueException {
        taskForce.addTag(new Tag(tagName));
        return this;
    }

    public TaskForce build() {
        return taskForce;
    }
}
