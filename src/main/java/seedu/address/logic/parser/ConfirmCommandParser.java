package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.IncorrectCommand;

public class ConfirmCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser
        .addNoFlagArg(CommandArgs.INDEX)
        .addRequiredArg(CommandArgs.START_DATETIME)
        .addRequiredArg(CommandArgs.END_DATETIME)
        .addOptionalArg(CommandArgs.DESC)
        .addOptionalArg(CommandArgs.TAGS) ;
        
        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
        }
        
        if (!parser.getArgValue(CommandArgs.INDEX).isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
        }
        
        int targetIndex = 0 ;
        
        try {
            targetIndex= Integer.parseInt(parser.getArgValue(CommandArgs.INDEX).get()) ;
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
        }
        
        String startDate = parser.getArgValue(CommandArgs.START_DATETIME).get() ;
        String endDate = parser.getArgValue(CommandArgs.END_DATETIME).get() ;

        try {
            
            return new ConfirmCommand(
                    targetIndex,
                    parser.getArgValue(CommandArgs.DESC).isPresent() ? parser.getArgValue(CommandArgs.DESC).get() : "",
                    startDate,
                    endDate,
                    parser.getArgValue(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValue(CommandArgs.TAGS).get()) : Collections.EMPTY_SET
                    );
        
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage()) ;
        }

    }
}
