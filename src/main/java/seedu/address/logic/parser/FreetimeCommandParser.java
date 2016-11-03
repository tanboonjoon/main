package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FreetimeCommand;
import seedu.address.logic.commands.IncorrectCommand;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.commons.exceptions.IncorrectCommandException;

//@@author A0139942W
/*
 *  parsing arguments and prepare them for freetime command
 */
// 
public class FreetimeCommandParser extends CommandParser {
    /**
     * Parses argument in the context of the freetime command
     * 
     * @param full command args string
     * @return the prepared command
     */
    private static final String SEARCH_TYPE = "day";
    private static final String SEPERATOR = "/";
    private static final boolean INVALID_ARGS = false;
    private static final boolean VALID_ARGS = true;
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_FREETIME_DAY = "0";
    private ArgumentsParser parser;

    @Override
    public Command prepareCommand(String args) {
        if (args.trim().equals(EMPTY_STRING)) {
            return new FreetimeCommand(DEFAULT_FREETIME_DAY);
        }
        prepareParser();
        if (!isValidArgs(SEARCH_TYPE, args.trim())) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FreetimeCommand.MESSAGE_USAGE));
        }
        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FreetimeCommand.MESSAGE_USAGE));
        }
        String parsedArgs = parser.getArgValue(CommandArgs.FIND_DAY).get().trim();
        if (!checkValidInt(parsedArgs)) {
            return new IncorrectCommand(
                    String.format(FreetimeCommand.INVALID_FREETIME_ARGS, FreetimeCommand.MESSAGE_USAGE));
        }
        return new FreetimeCommand(parsedArgs);
    }

    private void prepareParser() {
        parser = new ArgumentsParser();
        parser.addOptionalArg(CommandArgs.FIND_DAY);

    }

    private boolean isValidArgs(String searchType, String args) {
        int compareCharAt;
        char searchType_char;
        char args_char;
        int seperatorIndex = searchType.length();
        for (compareCharAt = 0; compareCharAt < searchType.length(); compareCharAt++) {
            searchType_char = searchType.toLowerCase().charAt(compareCharAt);
            args_char = args.toLowerCase().charAt(compareCharAt);
            if (searchType_char != args_char) {
                return INVALID_ARGS;
            }
        }
        return args.startsWith(SEPERATOR, seperatorIndex);
    }

    public boolean checkValidInt(String parsedArg) {
        try {
            Integer.parseInt(parsedArg);
        } catch (NumberFormatException e) {
            return INVALID_ARGS;
        }
        return VALID_ARGS;
    }

}
