package seedu.address.logic.commands;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    public final String feedbackToUser;
    public final boolean commandSuccess ;

    public CommandResult(String feedbackToUser, boolean success) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
        this.commandSuccess = success ;
    }
    
    public CommandResult(String feedbackToUser) {
        this (feedbackToUser, false) ;
    }
    
    public boolean isSuccessfulCommand () {
        return this.commandSuccess ;
    }

}
