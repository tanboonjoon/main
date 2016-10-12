package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import seedu.address.commons.exceptions.IncorrectCommandException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.IncorrectCommand;
/*
 * parsing arguments and prepare them for find command
 */
public class FindCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    
    @Override
    public Command prepareCommand(String args) {
        ArgumentsParser parser = new ArgumentsParser() ;
        
        parser.addNoFlagArg(CommandArgs.NAME) ;
        
        try {
            parser.parse(args);
        } catch (IncorrectCommandException e) {
             return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                     FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = parser.getArgValue(CommandArgs.NAME).get().split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

}
