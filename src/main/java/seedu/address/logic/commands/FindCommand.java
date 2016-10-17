package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Finds and lists all tasks in taskForce whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
//@@author A0139942W
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: TYPE/KEYWORDS\n"
            + "Example: " + COMMAND_WORD + " all/meeting\n" 
            +  COMMAND_WORD + " day/3\n" 
            +  COMMAND_WORD + " week/-4";
    public final static String INVALID_FIND_DATE_MESSAGE = "Please enter valid number when search by day/week";
    private final String FIND_TYPE_ALL = "ALL";
    private final int VALID_NO_OF_DATES_ARGS = 1;
    private final int INTEGER_ARGS_INDEX = 0;
    private final Set<String> keywords;
    private final String findType;

    public FindCommand(Set<String> keywords, String findType) throws IllegalValueException {

    	if (!checkKeyWord(keywords, findType)) {
        	throw new IllegalValueException(INVALID_FIND_DATE_MESSAGE);
        }
    	this.keywords = keywords;
        this.findType = findType;
    }
    
    //This method ensure that keyword for type 'day' and 'week' contain only a integer number
    public boolean checkKeyWord(Set<String> keywords, String findType) {
    	if (findType.equals(FIND_TYPE_ALL)) {
    		return true;
    	}
    	if (keywords.size() != VALID_NO_OF_DATES_ARGS) {
    		return false;
    	}
    	List<String> getNumList = new ArrayList<String>(keywords);   	
    	try {
    		Integer.parseInt(getNumList.get(INTEGER_ARGS_INDEX));
    	}catch (NumberFormatException e ) {
    		return false;
    	}
    	return true;
    	
    }
//@@author A0139942W
    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(keywords, findType);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

}
