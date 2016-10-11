package seedu.address.model.task;

import java.time.LocalDateTime;

import seedu.address.model.tag.UniqueTagList;
/**
* A deadline is a task that has only a ending datetime
*
 */
public class Deadline extends Task {
	
	private final LocalDateTime endDate;

	public Deadline(String name, String description, LocalDateTime endDate, UniqueTagList tags) {
		super(name,description,tags);
		this.endDate = endDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	@Override
	public boolean isSameStateAs(ReadOnlyTask other) {
		return super.isSameStateAs(other) 
				&& other instanceof Deadline
				&& this.getEndDate().equals( ((Deadline) other).getEndDate() ) ;
	}
}
