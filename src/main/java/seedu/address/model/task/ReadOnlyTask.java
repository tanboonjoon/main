package seedu.address.model.task;

import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the TaskForce. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyTask {
    
    public static final String EMPTY_DESCRIPTION = "" ;

    public String getName();

    public String getDescription();

    public boolean getDoneStatus();

    /**
     * The returned TagList is a deep copy of the internal TagList, changes on
     * the returned list will not affect the task's internal tags.
     */
    public UniqueTagList getTags();

    /**
     * Gets the unique Task ID associated to this task.
     */
    public int getTaskId();

    /**
     * Returns true if both have the same state. (interfaces cannot override
     * .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                        && other.getName().equals(this.getName()) // state
                                                                  // checks here
                                                                  // onwards
                        && other.getDescription().equals(this.getDescription()))
                        && other.getTaskId() == this.getTaskId() && other.getDoneStatus() == this.getDoneStatus();
    }

    /**
     * Formats the task as text, showing all task details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        
        builder.append(getName()) ;
        
        if ( !EMPTY_DESCRIPTION.equals(getDescription()) ) {
            builder.append(" Description: ") ;
            builder.append(getDescription()) ;
        }
        
        if (!getTags().getInternalList().isEmpty()) {
            builder.append(" Tags: ") ;
            getTags().forEach(builder::append);
        }
        
        return builder.toString();
    }

    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
