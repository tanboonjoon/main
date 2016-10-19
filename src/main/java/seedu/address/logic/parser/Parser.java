package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BlockCommand;
import seedu.address.logic.commands.CdCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;

/**
 * Parses user input.
 */
public class Parser {
	
	private static final Logger logger = LogsCenter.getLogger(Parser.class);
	
	private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = // '-' dashes are reserved for delimiter prefixes
            Pattern.compile("^(?<name>[^\\/]+)"
                    + "((?<description>d\\/[^\\/]+))?"
                    + "(?<tagArguments>(?:e\\/[^\\/]+)*)$"); // variable number of tags
    
    private static Map<String, Class<? extends CommandParser>> commandRegistry = Maps.newHashMap();
    
    static {
        registerCommand (AddCommandParser.class, AddCommand.COMMAND_WORD);
        registerCommand (SelectCommandParser.class, SelectCommand.COMMAND_WORD);
        registerCommand (DeleteCommandParser.class, DeleteCommand.COMMAND_WORD);
        registerCommand (FindCommandParser.class, FindCommand.COMMAND_WORD);
        registerCommand (ClearCommandParser.class, ClearCommand.COMMAND_WORD);
        registerCommand (HelpCommandParser.class, HelpCommand.COMMAND_WORD);
        registerCommand (ListCommandParser.class, ListCommand.COMMAND_WORD);
        registerCommand (ExitCommandParser.class, ExitCommand.COMMAND_WORD);
        registerCommand (EditCommandParser.class, EditCommand.COMMAND_WORD);
        registerCommand (MarkCommandParser.class, MarkCommand.COMMAND_WORD);
        registerCommand (UndoCommandParser.class, UndoCommand.COMMAND_WORD);
        registerCommand (RedoCommandParser.class, RedoCommand.COMMAND_WORD);
        registerCommand (CdCommandParser.class, CdCommand.COMMAND_WORD);
        registerCommand (BlockCommandParser.class, BlockCommand.COMMAND_WORD);

    }
    
    /**
     * Registers all associated command word strings with the provided command parser class.
     * One command parser can be associated with multiple command words such as ("add", "schedule", etc)
     * 
     */
    public static void registerCommand(Class<? extends CommandParser> parser, String... command) {
    	
    	for (String word : command) {
    		commandRegistry.put(word, parser);
    	}	
    }
    
    public static CommandParser getParserFromCommandWord (String commandWord) {
    	
    	if (!commandRegistry.containsKey(commandWord)) {
    		return new IncorrectCommandParser();
    	}
    	
    	Class<? extends CommandParser> parser = commandRegistry.get(commandWord) ;
    	
    	try {
    		CommandParser commandParser = parser.newInstance() ;
    		
    		return commandParser ;
    	} catch (Exception e) {
    		return new IncorrectCommandParser() ;
    	}
    	
    }

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        
        //Preparer preparer = new Preparer(); // abstract class can't be created
        
        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        return getParserFromCommandWord(commandWord).prepareCommand(arguments);
        
    }        

}
