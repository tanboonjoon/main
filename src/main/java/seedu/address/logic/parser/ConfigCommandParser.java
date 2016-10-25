package seedu.address.logic.parser;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfigCommand;
import seedu.address.logic.commands.IncorrectCommand;

public class ConfigCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser
        .addNoFlagArg(CommandArgs.NAME)
        .addRequiredArg(CommandArgs.VALUES) ;
        
        try {
            parser.parse(args) ;
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfigCommand.MESSAGE_USAGE));
        }
        
        return new ConfigCommand() ;
        
    }

}
