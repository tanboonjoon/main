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
    public static boolean INCLUDE_MARK = true;
    public static boolean EXCLUDE_MARK = false;
    private ArgumentsParser parser;
    @Override
    public Command prepareCommand(String args) {
        
    	
    	parser = prepareParser() ;

        try {	

            parser.parse(args);
            final boolean checkMark = prepareMarkArgs();
            final String typeOfFind = prepareTypeOfFind();
            if (!isValidArgs(typeOfFind, args.trim())) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindCommand.MESSAGE_USAGE));
            }
            
            
            final  Set<String> keywordSet = prepareSets(typeOfFind);
            

            return new FindCommand(keywordSet, typeOfFind, checkMark);
            
        } catch (IncorrectCommandException e) {
             return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                     FindCommand.MESSAGE_USAGE));
        
        } catch (IllegalValueException e) {
			// TODO Auto-generated catch block
        	return new IncorrectCommand(e.getMessage());
		}
        
        // keywords delimited by whitespace



    }
    


	private Set<String> prepareSets(String typeOfFind) throws IncorrectCommandException {
		// TODO Auto-generated method stub
    	final String[] keywords = getKeywords(typeOfFind);
    	final Set<String> preparedKeywordSet = Sets.newHashSet(Arrays.asList(keywords));
    	preparedKeywordSet.remove(EMPTY_STRING);
		return preparedKeywordSet;
	}

	private ArgumentsParser prepareParser() {
		// TODO Auto-generated method stub
    	ArgumentsParser prepareParser = new ArgumentsParser();
    	prepareParser
    	.addOptionalArg(CommandArgs.FIND_NAME)
    	.addOptionalArg(CommandArgs.FIND_WEEK)
    	.addOptionalArg(CommandArgs.FIND_DAY)
    	.addOptionalArg(CommandArgs.FIND_DESC)
    	.addOptionalArg(CommandArgs.FIND_TAG)
    	.addOptionalArg(CommandArgs.FIND_MARK)
    	.addOptionalArg(CommandArgs.FIND_TYPE);
    	return prepareParser;
	}

	//To check that users does not enter anything between find command and search type
    //eg. find abcd all/KEYWORDS
    private boolean isValidArgs(String typeOfFind, String args) {
		// TODO Auto-generated method stub
    	int compareCharAt ;
    	for (compareCharAt = 0 ; compareCharAt < typeOfFind.length() ; compareCharAt++) {
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
		// TODO Auto-generated method stub
		String markArgs = getMarkArg();
		if ("true".equalsIgnoreCase(markArgs)) {
			System.out.println("hey");
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
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_TAG).isPresent()  ? "TAG"  : EMPTY_STRING;	
	}

	private String getDescArg() {
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_DESC).isPresent() ? "DESC" : EMPTY_STRING;
	}

	private String getDayArg() {
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_DAY).isPresent()  ? "DAY"  : EMPTY_STRING;
	}

	private String getWeekArg() {
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_WEEK).isPresent() ? "WEEK" : EMPTY_STRING;
	}

	private String getNameArg() {
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_NAME).isPresent() ? "NAME"  : EMPTY_STRING;
	}
	
    private String getTypeArg() {
		// TODO Auto-generated method stub
		return parser.getArgValue(CommandArgs.FIND_TYPE).isPresent() ? "TYPE" : EMPTY_STRING;
	}
	

	public String getTypeOfFind(String... args) throws IncorrectCommandException {
		List<String> typeOfFind = new ArrayList<String> (Arrays.asList(args));
    	typeOfFind.removeAll(Arrays.asList(EMPTY_STRING , NULL_STRING));
    	
        if(typeOfFind.size() != VALID_FIND_TYPE_NUMBER) {
        	throw new IncorrectCommandException() ;
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
    		return parser.getArgValue(CommandArgs.FIND_TYPE).get().split("\\s+");
    	default:
    		throw new IncorrectCommandException() ;
    	}
 


    

    }
    

}
