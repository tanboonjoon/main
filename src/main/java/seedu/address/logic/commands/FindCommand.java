package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * @@author A0139942W
 * 
 *          Finds and lists all tasks in taskForce whose name contains any of
 *          the argument keywords. Keyword matching is case sensitive.
 */

public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + COMMAND_WORD + " OPTION/KEYWORDS [mark/true]\n" + "Parameters: " + COMMAND_WORD
            + " TYPE/SEARCHTYPE\n" + "Example: " + COMMAND_WORD + " name/meeting\n" + COMMAND_WORD + " day/3\n"
            + COMMAND_WORD + " week/-4\n" + COMMAND_WORD + " tag/Done mark/true\n" + COMMAND_WORD
            + " desc/Done mark/true\n" + COMMAND_WORD + " type/all\n" + COMMAND_WORD + " type/mark\n" + COMMAND_WORD
            + " type/overdue";

    public final static String INVALID_FIND_DATE_MESSAGE = "Please enter valid number when search by day/week";
    public final static String INVALID_FIND_TYPE_MESSAGE = "Find type only support overdue / all / task . ";
    private final String FIND_TYPE_NAME = "NAME";
    private final String FIND_TYPE_TAG = "TAG";
    private final String FIND_TYPE_DESC = "DESC";
    private final String FIND_TYPE_TYPE = "TYPE";
    private final String FIND_TYPE_ALL = "all";
    private final String FIND_TYPE_OVERDUE = "overdue";
    private final String FIND_TYPE_MARK = "mark";

    private final int VALID_NO_OF_ARG = 1;
    private final int FIND_ARGS_INDEX = 0;

    private final boolean VALID_ARG = true;
    private final boolean INVALID_ARG = false;

    private final Set<String> keywords;
    private final String typeOfFind;
    private final boolean isMarkCheck;

    public FindCommand(Set<String> keywords, String typeOfFind, boolean isMarkCheck) throws IllegalValueException {
        if (!checkKeyWord(keywords, typeOfFind)) {
            throw new IllegalValueException(INVALID_FIND_DATE_MESSAGE);
        }
        this.keywords = keywords;
        this.typeOfFind = typeOfFind;
        this.isMarkCheck = isMarkCheck;
    }

    // This method ensure that keyword for type 'day' and 'week' contain only a
    // integer number
    public boolean checkKeyWord(Set<String> keywords, String typeOfFind) throws IllegalValueException {
        if (isSearchByKeywords(typeOfFind)) {
            return VALID_ARG;
        }

        if (isSearchByType(typeOfFind, keywords)) {
            return VALID_ARG;
        }

        if (keywords.size() != VALID_NO_OF_ARG) {
            return INVALID_ARG;
        }

        List<String> getNumList = new ArrayList<String>(keywords);
        try {
            Integer.parseInt(getNumList.get(FIND_ARGS_INDEX));
        } catch (NumberFormatException e) {
            return INVALID_ARG;
        }
        return VALID_ARG;

    }

    private boolean isSearchByType(String typeOfFind, Set<String> keywords) throws IllegalValueException {
        // TODO Auto-generated method stub
        if (!typeOfFind.equals(FIND_TYPE_TYPE)) {
            return false;
        }
        if (keywords.size() != VALID_NO_OF_ARG) {
            throw new IllegalValueException(INVALID_FIND_TYPE_MESSAGE);
        }

        List<String> findTypeList = new ArrayList<String>(keywords);
        String findType = findTypeList.get(FIND_ARGS_INDEX).trim();
        if (FIND_TYPE_ALL.equalsIgnoreCase(findType) || FIND_TYPE_OVERDUE.equalsIgnoreCase(findType)
                || FIND_TYPE_MARK.equalsIgnoreCase(findType)) {
            return VALID_ARG;
        }

        throw new IllegalValueException(INVALID_FIND_TYPE_MESSAGE);

    }

    public boolean isSearchByKeywords(String typeOfFind) {
        return typeOfFind.equals(FIND_TYPE_NAME) || typeOfFind.equals(FIND_TYPE_DESC)
                || typeOfFind.equals(FIND_TYPE_TAG);
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(keywords, typeOfFind, isMarkCheck);
        return new CommandResult(getMessageForTaskListShownSummary(model.getSortedFilteredTask().size()), true);
    }
}
