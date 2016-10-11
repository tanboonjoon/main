package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

public class TaskForceCommandExecutedEvent extends BaseEvent {
	
	public final Class<? extends Command> commandClass ;
	public final String commandText ;
	public final CommandResult result ;
	
	public TaskForceCommandExecutedEvent (Class<? extends Command> commandClass, String commandText, CommandResult result) {
		this.commandClass = commandClass ;
		this.commandText = commandText ;
		this.result = result ;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
