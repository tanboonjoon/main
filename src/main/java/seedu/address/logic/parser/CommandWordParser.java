package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;

public class CommandWordParser {
    
    public CommandWordParser() {
        
    }
//    
//    public Command parseCommand(String commandWord) {
//        switch (commandWord) {
//
//        case AddCommand.COMMAND_WORD:
//            return prepareAdd(arguments);
//
//        case SelectCommand.COMMAND_WORD:
//            return prepareSelect(arguments);
//
//        case DeleteCommand.COMMAND_WORD:
//            return prepareDelete(arguments);
//
//        case ClearCommand.COMMAND_WORD:
//            return new ClearCommand();
//
//        case FindCommand.COMMAND_WORD:
//            return prepareFind(arguments);
//
//        case ListCommand.COMMAND_WORD:
//            return new ListCommand();
//
//        case ExitCommand.COMMAND_WORD:
//            return new ExitCommand();
//
//        case HelpCommand.COMMAND_WORD:
//            return new HelpCommand();
//
//        default:
//            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
//        }
//    }
}
