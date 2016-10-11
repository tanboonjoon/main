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
	
	public Event(String name, String description,LocalDateTime startDate, LocalDateTime endDate, UniqueTagList tags) {
		super(name,description,tags);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	@Override
	public boolean isSameStateAs(ReadOnlyTask other) {
		return super.isSameStateAs(other) 
				&& other instanceof Event
				&& this.getStartDate().equals( ((Event) other).getStartDate() )
				&& this.getEndDate().equals( ((Event) other).getStartDate() ) ;
	}
	
	@Override
	public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Task Name: ")
                .append(getName())
                .append(" Description: ")
                .append(getDescription())
                .append(" From: ")
                .append(DateUtil.parseLocalDateTimeIntoString(getStartDate()) )
                .append(" To: ")
                .append(DateUtil.parseLocalDateTimeIntoString(getEndDate()) )
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
	
}
