package seedu.address.model.task;

import java.util.Date;

import seedu.address.model.tag.UniqueTagList;
/**
* A event is a task that have a start date and and a end date
*
 */
public class Event extends Task{

	private final Date startDate;
	private final Date endDate;
	
	public Event(String name, String description,Date startDate, Date endDate, UniqueTagList tags) {
		super(name,description,tags);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
}
