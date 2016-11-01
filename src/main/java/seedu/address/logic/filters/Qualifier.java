package seedu.address.logic.filters;

import seedu.address.model.task.ReadOnlyTask;

public interface Qualifier {

    boolean run(ReadOnlyTask task);

    String toString();
}
