package seedu.address.logic.filters;

import seedu.address.model.task.ReadOnlyTask;

public class AlwaysTrueQualifier implements Qualifier {

    @Override
    public boolean run(ReadOnlyTask task) {
        return true ;
    }

}
