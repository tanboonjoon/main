package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE_FORMAT;

import java.time.format.DateTimeParseException;
import java.util.Collections;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

public class AddCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    
	@Override
	public Command prepareCommand(String args) {
		ArgumentsParser parser = new ArgumentsParser() ;

		parser
		.addNoFlagArg(CommandArgs.NAME)
		.addOptionalArg(CommandArgs.DESC)
		.addOptionalArg(CommandArgs.TAGS) ;

		try {
			parser.parse(args);
		} catch (IncorrectCommandException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
		}

		try {
			return new AddCommand(
					parser.getArgValue(CommandArgs.NAME).get(),
					parser.getArgValue(CommandArgs.DESC).isPresent() ? parser.getArgValue(CommandArgs.DESC).get() : "",
					parser.getArgValue(CommandArgs.START_DATETIME).isPresent() ? parser.getArgValue(CommandArgs.START_DATETIME).get() : null,
					parser.getArgValue(CommandArgs.END_DATETIME).isPresent() ? parser.getArgValue(CommandArgs.END_DATETIME).get() : null,
					parser.getArgValues(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValues(CommandArgs.TAGS).get()) : Collections.emptySet()
					) ;
		} catch (IllegalValueException e) {
			return new IncorrectCommand(e.getMessage());
		} catch (DateTimeParseException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_DATE_FORMAT, AddCommand.MESSAGE_USAGE));
		}
	}

}
