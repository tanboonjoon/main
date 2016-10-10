package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;

public abstract class CommandParser {
	public abstract Command prepareCommand (String args) ;
}
