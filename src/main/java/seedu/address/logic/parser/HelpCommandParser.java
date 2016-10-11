package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.Command;

public class HelpCommandParser extends CommandParser {
    @Override
    public Command prepareCommand(String args) {
        return new HelpCommand();
    }
}
