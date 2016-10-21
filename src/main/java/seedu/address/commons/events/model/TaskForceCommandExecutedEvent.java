package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

/**
 * @@author A0135768R
 * This event is fired right after a command is executed.
 * 
 */
public class TaskForceCommandExecutedEvent extends BaseEvent {
	
	public final Command commandClass ;
	public final CommandResult result ;
	
	public TaskForceCommandExecutedEvent (Command commandClass, CommandResult result) {
		this.commandClass = commandClass ;
		this.result = result ;
		
	}

	@Override
	public String toString() {
	    return "Command Executed: " + Command.class.getSimpleName() + ". Command Success:" + result.isSuccessfulCommand() ;
	}

}
