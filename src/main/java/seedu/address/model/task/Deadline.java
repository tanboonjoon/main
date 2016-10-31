package seedu.address.model.task;

import java.time.LocalDateTime;

import seedu.address.commons.util.DateUtil;
import seedu.address.model.tag.UniqueTagList;

/**
 * A deadline is a task that has only a ending datetime
 *
 */
public class Deadline extends Task {

    private final LocalDateTime endDate;

    public Deadline(int taskId, String name, String description, LocalDateTime endDate, UniqueTagList tags,
            boolean doneStatus) {
        super(taskId, name, description, tags, doneStatus);
        this.endDate = endDate;
    }

    public Deadline(int taskId, String name, String description, LocalDateTime endDate, UniqueTagList tags) {
        this(taskId, name, description, endDate, tags, false);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isDeadlineOverdue() {
        return DateUtil.NOW.isAfter(endDate);
    }

    @Override
    public boolean isSameStateAs(ReadOnlyTask other) {
        return super.isSameStateAs(other) && other instanceof Deadline
                && this.getEndDate().equals(((Deadline) other).getEndDate());
    }

    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" Task Name: ").append(getName()).append(" Description: ")
                .append(getDescription()).append(" Due by: ")
                .append(DateUtil.parseLocalDateTimeIntoString(getEndDate())).append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
