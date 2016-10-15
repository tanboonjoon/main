package seedu.address.model.task;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Task in the taskForce.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private final String name;
    private final String description ;
    private boolean doneStatus;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null. 
     */
    
    
    public Task(String name, String description, UniqueTagList tags, boolean doneStatus) {
        assert !CollectionUtil.isAnyNull(name, tags);
        this.name = name;
        this.description = description ;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        this.doneStatus = doneStatus;
    }
    
    public Task(String name, String description, UniqueTagList tags) {
        this (name, description, tags, false);
    }
    
    public Task(String name, UniqueTagList tags) {
    	this (name, "", tags) ;
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDescription(), source.getTags(), source.getDoneStatus());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }
    
	@Override
	public boolean getDoneStatus() {
		return doneStatus;
	}

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, description, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }


}
