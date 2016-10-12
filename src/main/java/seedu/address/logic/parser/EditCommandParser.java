package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;

import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.IncorrectCommand;

public class EditCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {
        int index = 0;
        String name = "";
        ArgumentsParser parser = new ArgumentsParser() ;

        parser  .addNoFlagArg(CommandArgs.NAME)
        .addOptionalArg(CommandArgs.NAME)
        .addOptionalArg(CommandArgs.DESC)
        .addOptionalArg(CommandArgs.TAGS) ;

        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }        
        String[] strArray = parser.getArgValue(CommandArgs.NAME).get().split(" ");


        if(!strArray[0].isEmpty()){
            try {
                index = Integer.parseInt(strArray[0]);
            } catch (NumberFormatException e){
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
            try {
                name = parser.getArgValue(CommandArgs.NAME).get().replaceFirst(strArray[0], "").trim();
            } catch (NullPointerException e){
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
        }

        try {
            return new EditCommand(
                    index,
                    name,
                    parser.getArgValue(CommandArgs.DESC).isPresent() ? parser.getArgValue(CommandArgs.DESC).get() : "",
                            parser.getArgValues(CommandArgs.TAGS).isPresent() ? Sets.newHashSet(parser.getArgValues(CommandArgs.TAGS).get()) : Collections.emptySet()

                    );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
}

