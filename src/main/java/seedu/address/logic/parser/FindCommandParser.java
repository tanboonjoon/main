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
	public static String EMPTY_STRING = "";
    public static int VALID_FIND_TYPE_NUMBER = 1;
    public static int FIND_TYPE_INDEX = 0;
    public static String NULL_STRING = null;
    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser
        .addOptionalArg(CommandArgs.FIND_ALL)
        .addOptionalArg(CommandArgs.FIND_WEEK)
        .addOptionalArg(CommandArgs.FIND_DAY);

        try {	

            parser.parse(args);
            
            final String find_type = prepareFindTypes(
            		parser.getArgValue(CommandArgs.FIND_ALL).isPresent() ? "ALL"  : "",
            		parser.getArgValue(CommandArgs.FIND_WEEK).isPresent() ? "WEEK"  : "",
            		parser.getArgValue(CommandArgs.FIND_DAY).isPresent() ? "DAY" : ""
            		);
            
            final String[] keywords = getKeywords(find_type, parser);
            final Set<String> keywordSet = Sets.newHashSet(Arrays.asList(keywords));
            
            keywordSet.remove(EMPTY_STRING);
            return new FindCommand(keywordSet, find_type);
            
        } catch (IncorrectCommandException e) {
             return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                     FindCommand.MESSAGE_USAGE));
        
        } catch (IllegalValueException e) {
			// TODO Auto-generated catch block
        	return new IncorrectCommand(e.getMessage());
		}
        
        // keywords delimited by whitespace



    }
    
    public String prepareFindTypes(String...args ) throws IncorrectCommandException {
    	List<String> find_type = new ArrayList<String> (Arrays.asList(args));
    	find_type.removeAll(Arrays.asList(EMPTY_STRING , NULL_STRING));
    	
        if(find_type.size() != VALID_FIND_TYPE_NUMBER) {
        	throw new IncorrectCommandException() ;
        }
        
        return find_type.get(FIND_TYPE_INDEX);
    	
    }
    


    

    public String[] getKeywords(String find_type, ArgumentsParser parser) throws IncorrectCommandException {
  
    	
    	switch (find_type) {
    	case "ALL":
    		return parser.getArgValue(CommandArgs.FIND_ALL).get().split("\\s+");
    	case "WEEK":
    		return parser.getArgValue(CommandArgs.FIND_WEEK).get().split("\\s+"); 		
    	case "DAY":
    		return parser.getArgValue(CommandArgs.FIND_DAY).get().split("\\s+");
    	default:
    		throw new IncorrectCommandException() ;
    	}
 


    

    }
    

}
