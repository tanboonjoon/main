package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javafx.util.Pair;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class MarkCommand extends Command {

	public static final String COMMAND_WORD = "mark";
	
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the task identified by the index number used in the last task listing as done.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_TASK_SUCCESS_DONE = "Marked Task : [%1$s] as done - ";
    public static final String MESSAGE_MARK_TASK_SUCCESS_UNDONE = "Marked Task : [%1$s] as undone - ";
    
    public final int targetIndex;
    private final List<ReadOnlyTask> tasksAdded = Lists.newLinkedList();
    private final List<ReadOnlyTask> tasksDeleted = Lists.newLinkedList();
    public MarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        
        ReadOnlyTask taskToMark ;
        
        try {
            taskToMark = lastShownList.get(targetIndex - 1) ; // List is 0-indexed
        
        } catch (IndexOutOfBoundsException e) {
            
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
       
        Task newTask = createNewTask (taskToMark.getName(), taskToMark.getDescription(), taskToMark.getTags(), 
        							  getStartDate(taskToMark), getEndDate(taskToMark), !taskToMark.getDoneStatus());

        
        try {
            model.addTask(newTask);
            tasksAdded.add(newTask);

            try{
                model.deleteTask(taskToMark);
                tasksDeleted.add(taskToMark);

            } catch (TaskNotFoundException pnfe) {
                assert false : "The target task cannot be missing";
            }
            
            if (newTask.getDoneStatus()) {
            	return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS_DONE, newTask.getName()), true);
            } else {
            	return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS_UNDONE, newTask.getName()), true);
            }
  
        } catch (UniqueTaskList.DuplicateTaskException e) {
           
            return new CommandResult(String.format("Error: duplicate message"));
        }
       
    }
    
    private Task createNewTask (String name, String description, UniqueTagList tag, LocalDateTime startTime, LocalDateTime endTime, boolean doneStatus) {
        
        assert model != null ;
        
        int id = model.getNextTaskId() ;
        
        if (startTime != null && endTime != null) {
            return new Event (id, name, description, startTime, endTime, tag, doneStatus) ;
        
        } 
        
        if (endTime != null && startTime == null) {
            return new Deadline (id, name, description, endTime, tag, doneStatus) ;
        
        } 
        
        return new Task (id, name, description, tag, doneStatus) ;
    }
    
    private LocalDateTime getStartDate (ReadOnlyTask taskToEdit) {
        if (taskToEdit instanceof Event) {
        	return ((Event) taskToEdit).getStartDate();
        }

        return null ;
    }
    
    private LocalDateTime getEndDate (ReadOnlyTask taskToEdit) {

        if (taskToEdit instanceof Deadline) {
        	return ((Deadline) taskToEdit).getEndDate();
        } 

        return null ;
    }
    @Override
    public Pair<List<ReadOnlyTask>, List<ReadOnlyTask>> getCommandChanges() {
        return new Pair<List<ReadOnlyTask>, List<ReadOnlyTask>>(ImmutableList.copyOf(tasksAdded), ImmutableList.copyOf(tasksDeleted)) ; 
    }
}
