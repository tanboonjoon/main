package seedu.address.logic.parser;

import com.google.common.collect.ImmutableList;

public enum CommandArgs {
    NAME(""),
    INDEX(""),
    DESC("d/"),
    TAGS("t/", "tag/"),
    START_DATETIME ("st/"),
    END_DATETIME ("et/"),

    RECURRING("recurring/", "recur/"),
    REPETITION("repeat/", "r/"),

    //Arguments for find command to parse
    FIND_DAY ("day/"),
    FIND_WEEK ("week/"),
    FIND_NAME ("name/"),
    FIND_DESC ("desc/"),
    FIND_TAG ("tag/"),

    // Special NULL flag to indicate useless arguments
    NULL_ARG("^/")

    ;

    private String[] commandString ;

    private CommandArgs (String... cmd) {
        commandString = cmd ;
    }

    @Override
    public String toString() {
        return commandString[0] ;
    }

    public Iterable<String> getAliases() {
        return ImmutableList.copyOf(commandString) ;
    }
}
