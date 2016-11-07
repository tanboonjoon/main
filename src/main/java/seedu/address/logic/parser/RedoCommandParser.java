package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.RedoCommand;
// @@author A0140037W
public class RedoCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        return new RedoCommand();
    }

}
