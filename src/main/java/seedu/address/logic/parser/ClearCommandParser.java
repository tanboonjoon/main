package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
/*
 *  Parsing command into clear command
 */
public class ClearCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        return new ClearCommand();
    }

}
