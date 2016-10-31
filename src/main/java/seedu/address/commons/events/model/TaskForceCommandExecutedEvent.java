package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

/**
 * @@author A0135768R 
 * <p>
 * This event is fired right after a command is executed.
 * 
 */
public class TaskForceCommandExecutedEvent extends BaseEvent {

    public final Command commandInstance ;
    public final CommandResult result ;

    public TaskForceCommandExecutedEvent (Command commandInstance, CommandResult result) {
        this.commandInstance = commandInstance ;
        this.result = result ;

    }

    @Override
    public String toString() {
        return "Command Executed: " + commandInstance.getClass().getSimpleName() + ". Command Success:" + result.isSuccessfulCommand() ;
    }

}
