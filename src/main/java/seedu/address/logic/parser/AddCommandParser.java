package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE_FORMAT;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Optional;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
/**
 * 
 * Parse arguments and prepare them for addcommand
 *
 */
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

		parser = prepareParser(parser);
		
		try {
			parser.parse(args);
		} catch (IncorrectCommandException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
		}

		try {

			return new AddCommand(
			        AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.NAME), ""),
			        AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.DESC), "") ,
			        AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.START_DATETIME), null),
			        AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.END_DATETIME), null),
					parser.getArgValues(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValues(CommandArgs.TAGS).get()) : Collections.emptySet(),
					AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.RECURRING), null),
					AddCommandParser.<String>getArgValueFromOptional(parser.getArgValue(CommandArgs.REPETITION), null)
			        ) ;
		} catch (IllegalValueException e) {
			return new IncorrectCommand(e.getMessage());
		} catch (DateTimeParseException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_DATE_FORMAT, AddCommand.MESSAGE_USAGE));
		}
	}

	private ArgumentsParser prepareParser(ArgumentsParser parser) {
		// TODO Auto-generated method stub
		parser.addNoFlagArg(CommandArgs.NAME)
		.addOptionalArg(CommandArgs.DESC)
		.addOptionalArg(CommandArgs.TAGS) 
		.addOptionalArg(CommandArgs.START_DATETIME)
		.addOptionalArg(CommandArgs.END_DATETIME)
		.addOptionalArg(CommandArgs.RECURRING)
		.addOptionalArg(CommandArgs.REPETITION);
		return parser;
	}
	
	// @@author A0135768R
	private static <T> T getArgValueFromOptional (Optional<T> argValue, T defaultValue) {
	    
	    if (argValue.isPresent()) {
	        return argValue.get() ;
	    }
	    
	    return defaultValue ;
	}

}
