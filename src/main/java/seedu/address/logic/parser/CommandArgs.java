package seedu.address.logic.parser;

public enum CommandArgs {
    NAME(""),
    INDEX(""),
    DESC("d/"),
    TAGS("t/"),
    START_DATETIME ("st/"),
    END_DATETIME ("et/"),
    
    
    RECURRING("recurring/"),
    REPETITION("repeat/"),
    
    //Arguments for find command to parse
    FIND_DAY ("day/"),
    FIND_WEEK ("week/"),
    FIND_ALL ("all/"),

    // Special NULL flag to indicate useless arguments
    NULL_ARG("^/")

    ;


    private String commandString ;

    private CommandArgs (String cmd) {
        commandString = cmd ;
    }

    @Override
    public String toString() {
        return commandString ;
    }
}
