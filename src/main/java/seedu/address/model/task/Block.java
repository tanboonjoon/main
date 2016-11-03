package seedu.address.model.task;

import java.time.LocalDateTime;

import seedu.address.model.tag.UniqueTagList;

// @@author A0135768R
/**
 * 
 *          A block is a special event that is unconfirmed.
 *
 */
public class Block extends Event {

    public Block(int taskId, String name, LocalDateTime startDate, LocalDateTime endDate) {
        super(taskId, name, "", startDate, endDate, new UniqueTagList());
    }

}
