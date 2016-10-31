package seedu.address.logic.parser;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfigCommand;
import seedu.address.logic.commands.IncorrectCommand;

// @@author A0135768R
/**
 * Command parser for the config command
 * 
 */
public class ConfigCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser();

        parser.addNoFlagArg(CommandArgs.NAME).addRequiredArg(CommandArgs.VALUES);

        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfigCommand.MESSAGE_USAGE));
        }

        try {
            return new ConfigCommand(parser.getArgValue(CommandArgs.NAME).get(),
                    parser.getArgValue(CommandArgs.VALUES).get());

        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }

    }

}
