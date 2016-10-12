package seedu.address.logic.parser;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
/*
 * parse into incorrectCommand command
 */
public class IncorrectCommandParser extends CommandParser {

	@Override
	public Command prepareCommand(String args) {
		return new IncorrectCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
	}

}
