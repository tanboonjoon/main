package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = // '-' dashes are reserved for delimiter prefixes
            Pattern.compile("^(?<name>[^\\/]+)"
                    + "((?<description>d\\/[^\\/]+))?"
                    + "(?<tagArguments>(?:t\\/[^\\/]+)*)$"); // variable number of tags
    
    
    private static final Pattern TASK_EDIT_DATA_ARGS_FORMAT = // '-' dashes are reserved for delimiter prefixes
            Pattern.compile("(?<index>)" 
                    + "(?<name>[^\\/]+)?"
                ); // variable number of tags);
                    
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

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);
            
        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);
            
        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
    	
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
    				parser.getArgValues(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValues(CommandArgs.TAGS).get()) : Collections.emptySet()
    		) ;
    	} catch (IllegalValueException e) {
    		 return new IncorrectCommand(e.getMessage());
    	}
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst("t/", "").split("t/"));
        return new HashSet<>(tagStrings);
    }
    
    /**
     * Extracts the new task's description from the add command's description arguments string.
     * 
     * @param rawDescription
     * @return the description as a string
     */
    private static String getDescriptionFromArgs(String args) {
    	if (args == null || args.equals("")) {
    		return "" ;
    	}
    	
    	Pattern pattern = Pattern.compile("d/(?<description>.*?(?=st/)|(?=et/)|(?=t/))");
    	Matcher matcher = pattern.matcher(args.trim());
    	return matcher.group("description").trim();
    }
    
    private static String getTitleFromArgs(String args){
        if (args == null || args.equals("")) {
            return "" ;
        }
        Pattern pattern = Pattern.compile("(?<name>\\D+.+$|(?=d/)|(?=st/)|(?=et/)|(?=t/))");
        Matcher matcher = pattern.matcher(args.trim());
        return matcher.group("name").trim();
    }
    
    /**
     * Extract index digit from edit command arguments string
     * @param args
     * @return the index as integer.
     */
    private static int getIndexFromArgs(String args) {
        if(args == null || args.equals("")){
            return 0;
        }
        Pattern pattern = Pattern.compile("^(?<index>\\d+)");
        Matcher matcher = pattern.matcher(args.trim());
        if(matcher.matches()){
            return Integer.parseInt(matcher.group("index"));
        }else{
            return 0;
        }
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
    	ArgumentsParser parser = new ArgumentsParser() ;
    	
    	parser.addNoFlagArg(CommandArgs.NAME) ;
    	
    	try {
    		parser.parse(args);
    	} catch (IncorrectCommandException e) {
    		 return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                     FindCommand.MESSAGE_USAGE));
    	}

        // keywords delimited by whitespace
        final String[] keywords = parser.getArgValue(CommandArgs.NAME).get().split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }
    
    private Command prepareEdit(String args) {
        int index = 0;
        String name = "";
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser  .addNoFlagArg(CommandArgs.NAME)
                .addOptionalArg(CommandArgs.NAME)
                .addOptionalArg(CommandArgs.DESC)
                .addOptionalArg(CommandArgs.TAGS) ;
        
        try {
                parser.parse(args);
        } catch (IncorrectCommandException e) {
                 return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }        
        String[] strArray = parser.getArgValue(CommandArgs.NAME).get().split(" ");
        
        
        if(!strArray[0].isEmpty()){
            try {
                index = Integer.parseInt(strArray[0]);
            } catch (NumberFormatException e){
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
            try {
                name = parser.getArgValue(CommandArgs.NAME).get().replaceFirst(strArray[0], "").trim();
            } catch (NullPointerException e){
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
        }

        try {
            return new EditCommand(
                    index,
                    name,
                    parser.getArgValue(CommandArgs.DESC).isPresent() ? parser.getArgValue(CommandArgs.DESC).get() : "",
                    parser.getArgValues(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValues(CommandArgs.TAGS).get()) : Collections.emptySet()
   
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

}