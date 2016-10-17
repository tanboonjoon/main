package seedu.address.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Lists;

import javafx.collections.ObservableList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskForce implements ReadOnlyTaskForce {

    private final UniqueTaskList tasks;
    private final UniqueTagList tags;

    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public TaskForce() {}

    /**
     * Persons and Tags are copied into this taskforce
     */
    public TaskForce(ReadOnlyTaskForce toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Persons and Tags are copied into this taskforce
     */
    public TaskForce(UniqueTaskList tasks, UniqueTagList tags) {
        resetData(tasks.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlyTaskForce getEmptyTaskForce() {
        return new TaskForce();
    }

//// list overwrite operations

    public ObservableList<Task> getTasks() {
        return tasks.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.getInternalList().setAll(tasks);
        this.tasks.getInternalList().sort(new TaskIdentificationCompartor());
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.getInternalList().setAll(tags);
    }

    public void resetData(Collection<? extends ReadOnlyTask> newTasks, Collection<Tag> newTags) {

        List<Task> tasks = Lists.newLinkedList() ;
        
        for (ReadOnlyTask thisTask : newTasks) {
            String name = thisTask.getName() ;
            String description = thisTask.getDescription() ;
            int id = thisTask.getTaskId() ;
            UniqueTagList tags = thisTask.getTags() ;
            
            if (thisTask instanceof Deadline) {
                LocalDateTime end = ((Deadline) thisTask).getEndDate() ;
                
                tasks.add(new Deadline (id, name, description, end, tags)) ;
            
            } else if (thisTask instanceof Event) {
                LocalDateTime start = ((Event) thisTask).getStartDate() ;
                LocalDateTime end = ((Event) thisTask).getEndDate() ;
                
                tasks.add(new Event (id, name, description, start, end, tags)) ;
            
            } else {
                tasks.add(new Task (id, name, description, tags)) ;
            }
        }
        
        setTasks (tasks) ;
        setTags (newTags);
    }

    public void resetData(ReadOnlyTaskForce newData) {
        resetData(newData.getTaskList(), newData.getTagList());
    }

//// task-level operations

    /**
     * Adds a task to the address book.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        syncTagsWithMasterList(p);
        tasks.add(p);
    }

    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : tags) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of task tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : taskTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        task.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return tasks.getInternalList().size() + " tasks, " + tags.getInternalList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return Collections.unmodifiableList(tasks.getInternalList());
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags.getInternalList());
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasks;
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tags;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskForce // instanceof handles nulls
                && this.tasks.equals(((TaskForce) other).tasks)
                && this.tags.equals(((TaskForce) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks, tags);
    }
    
    public int getNextTagId() {
        if (!this.tasks.getInternalList().isEmpty()) {
            int size = this.tasks.getInternalList().size() ;
            int id = this.tasks.getInternalList().get(size - 1).getTaskId() ;
            
            return id + 1 ;
        }
        
        return 0 ;
    }
    
    /*
     * Comparator for Tasks Id sorting
     */
    
    private static class TaskIdentificationCompartor implements Comparator<ReadOnlyTask> {

        @Override
        public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
            if (o1.getTaskId() < o2.getTaskId()) {
                return -1 ;
            }
            
            if (o1.getTaskId() > o2.getTaskId()) {
                return 1 ;
            }
            
            return 0 ;
        }
        
    }
}
