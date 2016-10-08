package seedu.address.model.task;

import java.util.Date;

import seedu.address.model.tag.UniqueTagList;
/**
* A deadline is a task that has only a end date 
*
 */
public class Deadline extends Task {
	
	private final Date endDate;

	public Deadline(String name, String description, Date endDate, UniqueTagList tags) {
		super(name,description,tags);
		this.endDate = endDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	

}
