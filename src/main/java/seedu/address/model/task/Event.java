package seedu.address.model.task;

import java.time.LocalDateTime;

import seedu.address.commons.util.DateUtil;
import seedu.address.model.tag.UniqueTagList;

/**
 * A event is a task that has a start datetime and and a end datetime
 *
 */
public class Event extends Task {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Event(int taskId, String name, String description, LocalDateTime startDate, LocalDateTime endDate,
            UniqueTagList tags, boolean doneStatus) {
        super(taskId, name, description, tags, doneStatus);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(int taskId, String name, String description, LocalDateTime startDate, LocalDateTime endDate,
            UniqueTagList tags) {
        this(taskId, name, description, startDate, endDate, tags, false);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean isSameStateAs(ReadOnlyTask other) {
        return super.isSameStateAs(other) && other instanceof Event
                && this.getStartDate().equals(((Event) other).getStartDate())
                && this.getEndDate().equals(((Event) other).getStartDate());
    }

    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        
        builder.append(super.getAsText()) ;
        builder.append(" From: ") ;
        builder.append(DateUtil.parseLocalDateTimeIntoString(getStartDate())) ;
        builder.append(" To: ") ;
        builder.append(DateUtil.parseLocalDateTimeIntoString(getEndDate())) ;
        return builder.toString();
    }

}
