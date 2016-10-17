package seedu.address.testutil;

import java.time.LocalDateTime;

import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.ReadOnlyTask;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private String name;
    private String description ;
    private UniqueTagList tags;
    private boolean doneStatus;
    private LocalDateTime[] dates ;

    public TestTask() {
        tags = new UniqueTagList();
        dates = new LocalDateTime[2] ;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription (String des) {
    	this.description = des ;
    }
    
    public void setStartDate (LocalDateTime date) {
        dates[0] = date ;
    }
    
    public void setEndDate (LocalDateTime date) {
        dates[1] = date ;
    }
    
    public LocalDateTime getStartDate () {
        return dates[0] ;
    }
    
    public LocalDateTime getEndDate () {
        return dates[1] ;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
    	return description ;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName());
        sb.append(" d/ " + this.getDescription() + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("t/ " + s.tagName + " "));
        return sb.toString();
    }
    
    public String getEditCommand(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("edit "+ index + " " + this.getName());
        sb.append(" d/ " + this.getDescription() + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("e/ " + s.tagName + " "));
        return sb.toString();
    }

	@Override
	public boolean getDoneStatus() {
		return doneStatus;
	}
}
