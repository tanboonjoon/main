package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.BlockCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

// @@author A0135768R
public class BlockCommandParser extends CommandParser{

    @Override
    public Command prepareCommand(String args) {

        ArgumentsParser parser = new ArgumentsParser(true) ;
        
        parser
        .addNoFlagArg(CommandArgs.NAME)
        .addRequiredArg(CommandArgs.END_DATETIME)
        .addRequiredArg(CommandArgs.START_DATETIME) ;
        
        try {
            parser.parse(args) ;
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BlockCommand.MESSAGE_USAGE));
        }
        
        List<LocalDateTime> startDateTimes = convertArgsToDateTime (parser, CommandArgs.START_DATETIME) ;
        List<LocalDateTime> endDateTimes = convertArgsToDateTime (parser, CommandArgs.END_DATETIME) ;
        
        if (startDateTimes.size() != endDateTimes.size()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BlockCommand.MESSAGE_USAGE));
        }
        
        try {
            return new BlockCommand(parser.getArgValue(CommandArgs.NAME).get(), startDateTimes, endDateTimes) ;
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }
    
    private List<LocalDateTime> convertArgsToDateTime (ArgumentsParser parser, CommandArgs date) {
        
        assert parser != null ;
        
        List<LocalDateTime> result = Lists.newLinkedList() ;
        
        if (parser.getArgValue(date).isPresent()) {
            
            for (String dateString : parser.getArgValues(date).get()) {
                Optional<LocalDateTime> dateTime = DateUtil.parseStringIntoDateTime(dateString) ;
                
                if (dateTime.isPresent()) {
                    result.add(dateTime.get()) ;
                }
                
            }
        }
        
        return result ;
    }
}
