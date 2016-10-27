package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.IncorrectCommand;

/*
 * @@author: A0111277M
 */

public class MarkCommandParser extends CommandParser {
    /**
     * Parses arguments in the context of the mark task as done command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    
    @Override
    public Command prepareCommand(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        return new MarkCommand(index.get());
    }
}
