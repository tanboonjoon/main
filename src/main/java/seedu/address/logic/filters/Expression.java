package seedu.address.logic.filters;

import seedu.address.model.task.ReadOnlyTask;

public interface Expression {
    
    boolean satisfies(ReadOnlyTask task);

    String toString();
}
