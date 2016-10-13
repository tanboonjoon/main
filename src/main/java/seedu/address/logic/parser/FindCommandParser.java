package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.IncorrectCommand;
/*
 * parsing arguments and prepare them for find command
 */
public class FindCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public static int VALID_FIND_TYPE_NUMBER = 1;
    public static int FIND_TYPE_INDEX = 0;
    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser.addOptionalArg(CommandArgs.FIND_ALL)
        .addOptionalArg(CommandArgs.FIND_EVENT)
        .addOptionalArg(CommandArgs.FIND_DEADLINE)
        .addOptionalArg(CommandArgs.FIND_TASK)
        .addOptionalArg(CommandArgs.FIND_WEEK)
        .addOptionalArg(CommandArgs.FIND_DAY);
        

        try {

            parser.parse(args);
            final String find_type = prepareFindTypes(
            		parser.getArgValue(CommandArgs.FIND_ALL).isPresent() ? "ALL"  : "",
            		parser.getArgValue(CommandArgs.FIND_EVENT).isPresent() ? "EVENT" : "" ,
            		parser.getArgValue(CommandArgs.FIND_DEADLINE).isPresent() ? "DEADLINE"  : "",
            		parser.getArgValue(CommandArgs.FIND_TASK).isPresent() ? "TASK" : "",
            		parser.getArgValue(CommandArgs.FIND_WEEK).isPresent() ? "WEEK"  : "",
            		parser.getArgValue(CommandArgs.FIND_DAY).isPresent() ? "DAY" : ""
            		);
            
            final String[] keywords = getKeywords(find_type, parser);
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
            return new FindCommand(keywordSet, find_type);
        } catch (IncorrectCommandException e) {
             return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                     FindCommand.MESSAGE_USAGE));
        }
        
        // keywords delimited by whitespace

    }
    
    public String prepareFindTypes(String...args ) throws IncorrectCommandException {
    	List<String> find_type = new ArrayList<String> (Arrays.asList(args));
    	find_type.removeAll(Arrays.asList("" , null));
    	
    	
        if(find_type.size() != VALID_FIND_TYPE_NUMBER) {
        	throw new IncorrectCommandException() ;
        }
        
        return find_type.get(FIND_TYPE_INDEX);
    	
    }
    
<<<<<<< 7da5dadb0c00bb8601807684006821886bf75935
    public String[] getKeywords(String find_type, ArgumentsParser parser) throws IncorrectCommandException {
  
    	
    	switch(find_type) {
    	case "ALL":
    		return parser.getArgValue(CommandArgs.FIND_ALL).get().split("\\s+");
    	case "EVENT":
    		return parser.getArgValue(CommandArgs.FIND_EVENT).get().split("\\s+");
    	case "DEADLINE":
    		return parser.getArgValue(CommandArgs.FIND_DEADLINE).get().split("\\s+");
    	case "TASK":
    		return parser.getArgValue(CommandArgs.FIND_TASK).get().split("\\s+");
    	case "WEEK":
    		return parser.getArgValue(CommandArgs.FIND_WEEK).get().split("\\s+"); 		
    	case "DAY":
    		return parser.getArgValue(CommandArgs.FIND_DAY).get().split("\\s+");
    	default:
    		throw new IncorrectCommandException() ;
    	}
 
=======
    public String prepareArgument(String...args ) {
    	System.out.println(args.length);
    	return "asd";
    	
>>>>>>> test
    }
    

}
