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
//@@author A0139942W
public class FindCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
	public static String EMPTY_STRING = "";
    public static int VALID_FIND_TYPE_NUMBER = 1;
    public static int FIND_TYPE_INDEX = 0;
    public static String NULL_STRING = null;
    public static String SEPERATOR = "/";
    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser
        .addOptionalArg(CommandArgs.FIND_NAME)
        .addOptionalArg(CommandArgs.FIND_WEEK)
        .addOptionalArg(CommandArgs.FIND_DAY)
        .addOptionalArg(CommandArgs.FIND_DESC)
        .addOptionalArg(CommandArgs.FIND_TAG);

        try {	

            parser.parse(args);
            
            final String find_type = prepareFindTypes(
            		parser.getArgValue(CommandArgs.FIND_NAME).isPresent() ? "NAME"  : "",
            		parser.getArgValue(CommandArgs.FIND_WEEK).isPresent() ? "WEEK" : "",
            		parser.getArgValue(CommandArgs.FIND_DAY).isPresent()  ? "DAY"  : "",
            		parser.getArgValue(CommandArgs.FIND_DESC).isPresent() ? "DESC" : "",
            		parser.getArgValue(CommandArgs.FIND_TAG).isPresent()  ? "TAG"  : ""
            				
            		);
            if (!isValidArgs(find_type, args.trim())) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindCommand.MESSAGE_USAGE));
            }
            
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
    
    //To check that users does not enter anything between find command and search type
    //eg. find abcd all/KEYWORDS
    private boolean isValidArgs(String find_type, String args) {
		// TODO Auto-generated method stub
    	int compareCharAt ;
    	for (compareCharAt = 0 ; compareCharAt < find_type.length() ; compareCharAt++) {
    		char findType_char = find_type.toLowerCase().charAt(compareCharAt);
    		char args_char = args.toLowerCase().charAt(compareCharAt);
 
    		if (findType_char != args_char) {
    			return false;
    		}
    	}
    	
		int seperatorIndex = compareCharAt++;
		return args.startsWith(SEPERATOR, seperatorIndex);
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
    	default:
    		throw new IncorrectCommandException() ;
    	}
 


    

    }
    

}
