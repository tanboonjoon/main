package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.IncorrectCommand;

/*
 * @@author A0135768R
 * 
 * parsing arguments to and preparing them for delete command
 */
public class DeleteCommandParser extends CommandParser {

    @Override
    public Command prepareCommand(String args) {

        String[] indexes = args.split(",");
        List<Integer> indexList = Lists.newLinkedList();

        for (String i : indexes) {
            Optional<Integer> index = parseIndex(i);

            if (!index.isPresent()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }

            indexList.add(index.get());
        }

        Integer[] indexArray = new Integer[indexList.size()];

        return new DeleteCommand(indexList.toArray(indexArray));
    }
}
