package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.*;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(name);
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withDescription(String address) throws IllegalValueException {
        this.task.setDescription(address);
        return this;
    }

    public TaskBuilder withPhone(String phone) throws IllegalValueException {
//        this.person.setPhone(new Phone(phone));
        return this;
    }

    public TaskBuilder withEmail(String email) throws IllegalValueException {
//        this.person.setEmail(new Email(email));
        return this;
    }

    public TaskBuilder withRecurring(String recur) throws IllegalValueException {
        this.task.setRecurring(recur);
        return this;
    }
    
    public TaskBuilder withRepeat(int rep) throws IllegalValueException {
        this.task.setRepeat(rep);
        return this;
    }
    
    public TestTask build() {
        return this.task;
    }

}
