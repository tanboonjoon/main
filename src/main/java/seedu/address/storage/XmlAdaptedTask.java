package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

import javax.xml.bind.annotation.XmlElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {
    
    @XmlElement(required = true)
    private int taskId ;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement
    private String startDateTime;
    @XmlElement
    private String endDateTime;
    @XmlElement
    private boolean doneStatus;
    @XmlElement
    private boolean isBlock ;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        name = source.getName();
        description = source.getDescription() ;
        taskId = source.getTaskId() ;
        doneStatus = source.getDoneStatus();
        tagged = new ArrayList<>();
        
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        
        if (source instanceof Deadline) {
            endDateTime = ((Deadline) source).getEndDate().toString() ;
        }
        
        if (source instanceof Event ) {
            startDateTime = ((Event) source).getStartDate().toString() ;
            endDateTime = ((Event) source).getEndDate().toString() ;
        }
        
        if (source instanceof Block) {
            isBlock = true ;
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {
        
        Task task ;
        LocalDateTime start = null ;
        LocalDateTime end = null ;
        
        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        
        final String name = this.name ;
        final String description = this.description ;
        final boolean doneStatus = this.doneStatus;
        final boolean isBlock = this.isBlock ;
        final UniqueTagList tags = new UniqueTagList(taskTags);
        final int id = this.taskId ;
        
        if (this.startDateTime != null) {
            start = LocalDateTime.parse(this.startDateTime) ;
        }
        
        if (this.endDateTime != null) {
            end = LocalDateTime.parse(this.endDateTime) ;
        }
        
        if (isBlock) {
            task = new Block(id, name, start, end) ;
        
        } else if (start != null && end != null) {
            task = new Event (id, name, description, start, end, tags, doneStatus) ; 
        
        } else if (start == null && end != null) {
            task = new Deadline (id, name, description, end, tags, doneStatus) ;
        
        } else {
            task = new Task(id, name, description, tags, doneStatus);
        }
        
        return task ;
    }
}
