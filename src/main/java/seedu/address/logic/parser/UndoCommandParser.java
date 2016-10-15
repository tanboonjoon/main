package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.UndoCommand;

public class UndoCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser();
                
        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
        }
        
        return new UndoCommand();
    }

}
