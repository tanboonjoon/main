package seedu.address.logic.filters;

import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

public class EventQualifier implements Qualifier {
    
    private final boolean filterEvent ;
    
    public EventQualifier (boolean eventsOnly) {
        filterEvent = eventsOnly ;
    }

    @Override
    public boolean run(ReadOnlyTask task) {
        return (filterEvent) ? task instanceof Event : !(task instanceof Event) ;
    }

}
