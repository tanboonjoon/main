package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CdCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

public class CdCommandParser extends CommandParser{

	@Override
	public Command prepareCommand(String args) {

		
		try {
			return new CdCommand (args.trim());
					
		} catch (IllegalValueException e) {
			return new IncorrectCommand(e.getMessage());
		}

	
	}

}

