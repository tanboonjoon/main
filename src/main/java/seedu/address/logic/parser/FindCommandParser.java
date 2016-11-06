package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.IncorrectCommand;


// @@author A0139942W
/*
 * parsing arguments and prepare them for find command
 */
public class FindCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the find task command.
     * FindCommand consist of three type of searches
     * Keywords : name , desc, tag
     * Day/week : day , week
     * TYPE : all , mark, overdue
     * @param args  full command args string
     *           
     * @return the prepared command
     */
    private static final String EMPTY_STRING = "";
    private static final String SEPERATOR = "/";
    private static final String CHECK_TRUE = "true";
    private static final String NULL_STRING = null;
    private static final int VALID_FIND_TYPE_NUMBER = 1;
    private  static final int FIND_TYPE_INDEX = 0;
    private static final boolean INCLUDE_MARK = true;
    private  static final boolean EXCLUDE_MARK = false;
    private ArgumentsParser parser;
    
    @Override
    public Command prepareCommand(String args) {
 
        try {
            parser = prepareParser(args);
            parser.parse(args);
            final boolean checkMark = prepareMarkArgs();
            final String typeOfFind = prepareTypeOfFind();
            if (!isValidArgs(typeOfFind, args.trim())) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            final Set<String> keywordSet = prepareSets(typeOfFind);
            return new FindCommand(keywordSet, typeOfFind, checkMark);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

    private Set<String> prepareSets(String typeOfFind) throws IncorrectCommandException {
        final String[] keywords = getKeywords(typeOfFind);
        final Set<String> preparedKeywordSet = Sets.newHashSet(Arrays.asList(keywords));
        preparedKeywordSet.remove(EMPTY_STRING);
        return preparedKeywordSet;
    }

    private ArgumentsParser prepareParser(String argsToParse) throws IncorrectCommandException {
        ArgumentsParser prepareParser = new ArgumentsParser();
        prepareParser.addOptionalArg(CommandArgs.FIND_NAME)
        .addOptionalArg(CommandArgs.FIND_WEEK)
        .addOptionalArg(CommandArgs.FIND_DAY)
        .addOptionalArg(CommandArgs.FIND_DESC)
        .addOptionalArg(CommandArgs.FIND_TAG)
        .addOptionalArg(CommandArgs.FIND_MARK)
        .addOptionalArg(CommandArgs.FIND_TYPE);
        return prepareParser;
    }

    // To check that users does not enter anything between find command and
    // search type
    // eg. find abcd all/KEYWORDS
    private boolean isValidArgs(String typeOfFind, String args) {
        int compareCharAt;
        for (compareCharAt = 0; compareCharAt < typeOfFind.length(); compareCharAt++) {
            char findType_char = typeOfFind.toLowerCase().charAt(compareCharAt);
            char args_char = args.toLowerCase().charAt(compareCharAt);
            if (findType_char != args_char) {
                return false;
            }
        }

        int seperatorIndex = compareCharAt++;
        return args.startsWith(SEPERATOR, seperatorIndex);
    }

    public String prepareTypeOfFind() throws IncorrectCommandException {
        String name = getNameArg();
        String week = getWeekArg();
        String day = getDayArg();
        String desc = getDescArg();
        String tag = getTagArg();
        String type = getTypeArg();
        return getTypeOfFind(name, week, day, desc, tag, type);

    }

    private boolean prepareMarkArgs() throws IncorrectCommandException {
        String markArgs = getMarkArg();
        if (CHECK_TRUE.equalsIgnoreCase(markArgs)) {
            return INCLUDE_MARK;
        }
        if (EMPTY_STRING.equals(markArgs)) {
            return EXCLUDE_MARK;
        }
        throw new IncorrectCommandException();
    }

    private String getMarkArg() {
        if (!parser.getArgValue(CommandArgs.FIND_MARK).isPresent()) {
            return EMPTY_STRING;
        }
        return parser.getArgValue(CommandArgs.FIND_MARK).get();
    }

    private String getTagArg() {
        return parser.getArgValue(CommandArgs.FIND_TAG).isPresent() ? "TAG" : EMPTY_STRING;
    }

    private String getDescArg() {
        return parser.getArgValue(CommandArgs.FIND_DESC).isPresent() ? "DESC" : EMPTY_STRING;
    }

    private String getDayArg() {
        return parser.getArgValue(CommandArgs.FIND_DAY).isPresent() ? "DAY" : EMPTY_STRING;
    }

    private String getWeekArg() {
        return parser.getArgValue(CommandArgs.FIND_WEEK).isPresent() ? "WEEK" : EMPTY_STRING;
    }

    private String getNameArg() {
        return parser.getArgValue(CommandArgs.FIND_NAME).isPresent() ? "NAME" : EMPTY_STRING;
    }

    private String getTypeArg() {
        return parser.getArgValue(CommandArgs.FIND_TYPE).isPresent() ? "TYPE" : EMPTY_STRING;
    }

    public String getTypeOfFind(String... args) throws IncorrectCommandException {
        List<String> typeOfFind = new ArrayList<String>(Arrays.asList(args));
        typeOfFind.removeAll(Arrays.asList(EMPTY_STRING, NULL_STRING));
        if (typeOfFind.size() != VALID_FIND_TYPE_NUMBER) {
            throw new IncorrectCommandException();
        }
        return typeOfFind.get(FIND_TYPE_INDEX);
    }

    public String[] getKeywords(String typeOfFind) throws IncorrectCommandException {
        switch (typeOfFind) {
        case "NAME":
            return parser.getArgValue(CommandArgs.FIND_NAME).get().split("\\s+");
        case "WEEK":
            return parser.getArgValue(CommandArgs.FIND_WEEK).get().split("\\s+");
        case "DAY":
            return parser.getArgValue(CommandArgs.FIND_DAY).get().split("\\s+");
        case "DESC":
            return parser.getArgValue(CommandArgs.FIND_DESC).get().split("\\s+");
        case "TAG":
            return parser.getArgValue(CommandArgs.FIND_TAG).get().split("\\s+");
        case "TYPE":
            if (parser.getArgValue(CommandArgs.FIND_MARK).isPresent()) {    //MARK filter is only allowed for keywords searched and day/week searched
                throw new IncorrectCommandException();
            }
            return parser.getArgValue(CommandArgs.FIND_TYPE).get().split("\\s+");
        default:
            throw new IncorrectCommandException();
        }

    }

}
