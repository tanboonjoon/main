package seedu.address.logic.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.collect.Iterables;

import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command {
    
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo ";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": undo the previous action.\n"
            + "Parameters: STEP (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    @Override
    public CommandResult execute() {

//        Set<Model> models = this.history.keySet();
//        this.model = Iterables.getLast(models, null);
//        String command = this.history.get(this.model);
       final ListIterator<String> it = this.commandHistory.listIterator(this.commandHistory.size());
       LinkedList<ReadOnlyTask> taskList = this.taskHistory.get(it.previous());
       ListIterator<ReadOnlyTask> taskIt = taskList.listIterator(taskList.size());
       if(it.hasPrevious()) {
           if(it.equals("add")){
              
               try {
                   model.deleteTask(taskIt.previous());
               } catch (TaskNotFoundException e) {
                   return new CommandResult("Nothing to undo!");
               }
           }
           else if(it.equals("delete")){
               try {
                   Task task = new Task(taskIt.previous());
                   model.addTask(task);
               } catch (NoSuchElementException e) {
                   return new CommandResult("Nothing to undo!");
               } catch (DuplicateTaskException e) {
                   return new CommandResult(AddCommand.MESSAGE_DUPLICATE_TASK);
            }
           }
           else {// edit 
               
           }
       }
       
        
        
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    @Override
    public boolean isUndoableCommand(){
        return true;
    }

}
