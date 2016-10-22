package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FreetimeCommand;
import seedu.address.logic.commands.IncorrectCommand;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.commons.exceptions.IncorrectCommandException;

/*
 *  parsing arguments and prepare them for freetime command
 */
//@author A0139942W
public class FreetimeCommandParser extends CommandParser {
	/**
	 * Parses argument in the context of the freetime command
	 * @param full command args string
	 * @return the prepared command
	 */
	public static final String SEARCH_TYPE = "day";
	public static final String SEPERATOR = "/";
	@Override
	public Command prepareCommand(String args) {
		// TODO Auto-generated method stub
		ArgumentsParser parser = new ArgumentsParser();
		parser.addRequiredArg(CommandArgs.FIND_DAY);
		
		if (!isValidArgs(SEARCH_TYPE, args.trim())) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FreetimeCommand.MESSAGE_USAGE));
		}
		
		try {
			parser.parse(args);
					
		} catch (IncorrectCommandException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FreetimeCommand.MESSAGE_USAGE));
		}
		String parsedArgs = parser.getArgValue(CommandArgs.FIND_DAY).get().trim();
		
		if (!checkValidInt(parsedArgs)) {
			return new IncorrectCommand(String.format(FreetimeCommand.INVALID_FREETIME_ARGS, FreetimeCommand.MESSAGE_USAGE));
		}
		

		return new FreetimeCommand(parsedArgs);
	}
	


	private boolean isValidArgs(String searchType, String args) {
		// TODO Auto-generated method stub
		int compareCharAt;
		char searchType_char;
		char args_char;
		int seperatorIndex = searchType.length();
		
		for (compareCharAt = 0; compareCharAt < searchType.length();  compareCharAt++) {
			searchType_char = searchType.toLowerCase().charAt(compareCharAt);
			args_char = args.toLowerCase().charAt(compareCharAt);
			
			if (searchType_char != args_char) {
				return false;
			}
		}	
		return args.startsWith(SEPERATOR, seperatorIndex);
	}

	public boolean checkValidInt(String parsedArg) {
		try {
			int getInteger = Integer.parseInt(parsedArg);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

}
