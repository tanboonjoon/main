package seedu.address.logic.commands;

import java.util.Set;

/**
 * Finds and lists all tasks in taskForce whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " meeting";

    private final Set<String> keywords;
    private final String findType;

    public FindCommand(Set<String> keywords, String findType) {
        this.keywords = keywords;
        this.findType = findType;
    }
    
    public boolean checkKeyWord(Set<String> keywords, String findType) {

    	switch(findType) {
    	case "ALL" :
    		break;
    	case "DAY" :
    		break;
    	case "Week" :
    		break;
    	}
    	return false;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(keywords);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

}
