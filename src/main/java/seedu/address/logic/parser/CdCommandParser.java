package seedu.address.logic.parser;


import java.io.IOException;

import org.json.simple.parser.ParseException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.CdCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

//@@author A0139942W
public class CdCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        String commandType = getType(args.trim());
        try {
            return new CdCommand(args.trim(), commandType);
        } catch (IllegalValueException e) {
            return new IncorrectCommand((e.getMessage() + "\n" + CdCommand.MESSAGE_USAGE));
        } catch (ParseException e) {
            return new IncorrectCommand(CdCommand.MESSAGE_FAILURE_PARSE);
        } catch (IOException e) {
            return new IncorrectCommand(CdCommand.MESSAGE_FAILURE_PARSE);
        }

    }

    public String getType(String args) {
        if (args.isEmpty()) {
            return CdCommand.CD_CHECK;
        }
        return CdCommand.CD_CHANGE;
    }

}
