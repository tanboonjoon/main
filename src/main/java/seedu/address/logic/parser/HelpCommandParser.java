package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.Command;
/*
 * parse into help command
 */
public class HelpCommandParser extends CommandParser {
    @Override
    public Command prepareCommand(String args) {
        return new HelpCommand();
    }
}
