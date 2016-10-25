package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import javafx.util.Pair;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.AddCommand;
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
        
        List<LocalDateTime> startDateTimes = Lists.newLinkedList() ;
        List<LocalDateTime> endDateTimes = Lists.newLinkedList() ;
        
        try {
            convertArgsToDateTime(parser, startDateTimes, endDateTimes) ;
        
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
        
        try {
            return new BlockCommand(parser.getArgValue(CommandArgs.NAME).get(), startDateTimes, endDateTimes) ;
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }
    
    private void convertArgsToDateTime (ArgumentsParser parser, List<LocalDateTime> startTimes, List<LocalDateTime> endTimes)
    throws IllegalValueException {
        
        assert parser != null ;
        
        List<String> startStrings = parser.getArgValues(CommandArgs.START_DATETIME).orElse(Collections.emptyList()) ;
        List<String> endStrings = parser.getArgValues(CommandArgs.END_DATETIME).orElse(Collections.emptyList()) ;
        
        if (!startStrings.isEmpty() && !endStrings.isEmpty() && startStrings.size() == endStrings.size()) {
            
            for (int i = 0; i < startStrings.size(); i ++) {
                Pair<LocalDateTime, LocalDateTime> startEndDates =  
                        DateUtil.determineStartAndEndDateTime(startStrings.get(i), endStrings.get(i))
                        .orElseThrow(() -> new IllegalValueException(AddCommand.INVALID_END_DATE_MESSAGE)) ;
                
                startTimes.add(startEndDates.getKey()) ;
                endTimes.add(startEndDates.getValue()) ;
            }
            
        } else {
            throw new IllegalValueException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BlockCommand.MESSAGE_USAGE)) ;
        }
        
    }
}
