package seedu.address.logic.commands;

import java.util.Set;

import com.google.common.collect.Sets;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer) NAME | d/DESCRIPTION | e/TAG...\n"
            + "Example: " + COMMAND_WORD + " 1 d/download How I Met Your Mother season 1" ;
    public static final String MESSAGE_EDIT_SUCCESS = "Edit saved!";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";

    private final int targetIndex;
    private final String name;
    private final String description;
    private final Set<Tag> tagSet;
    
    public EditCommand(int targetIndex, String name, String description, Set<String> tags) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.name = name;
        this.description = description;
        this.tagSet = Sets.newHashSet() ;
        
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }

    }
    @Override
    public CommandResult execute() {
        String newName, newDescription;
        UniqueTagList newTagSet;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        assert targetIndex < lastShownList.size(): targetIndex;
        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);
        
        try {
            model.deleteTask(taskToEdit);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
        if(!(name.equals(""))) {
            newName = name;
        }else{
            newName = taskToEdit.getName();
        }
        assert name.equals(""): "fail here";
        if(!description.equals("")) {
            newDescription = description;
        }else{
            newDescription = taskToEdit.getDescription();
        }
        
        if(!tagSet.isEmpty()) {
            newTagSet = new UniqueTagList(tagSet);
        }else{
            newTagSet = taskToEdit.getTags();
        }
        
        Task newTask = new Task(newName, newDescription, newTagSet);

        assert model != null;
        try {
            model.addTask(newTask);
            return new CommandResult(String.format(MESSAGE_EDIT_SUCCESS, newTask));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
    }

}
