package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CdCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

public class CdCommandParser extends CommandParser{

	@Override
	public Command prepareCommand(String args) {
		// TODO Auto-generated method stub
		ArgumentsParser parser = new ArgumentsParser() ;
		
		parser
		.addNoFlagArg(CommandArgs.NAME);


	
	try {
		parser.parse(args);
	} catch (IncorrectCommandException e) {
		return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CdCommand.MESSAGE_USAGE));
	}
	return null;
	}
}

