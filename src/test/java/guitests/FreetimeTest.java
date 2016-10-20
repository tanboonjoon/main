package guitests;

import org.junit.Test;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.logic.commands.FreetimeCommand;

//@@ A0139942W
public class FreetimeTest extends TaskForceGuiTest{
	
	@Test
	public void invalid_command() {
		String invalidCommand = String.format(MESSAGE_INVALID_COMMAND_FORMAT , FreetimeCommand.MESSAGE_USAGE);
		String invalidArgs = String.format(FreetimeCommand.INVALID_FREETIME_ARGS, FreetimeCommand.MESSAGE_USAGE);
		commandBox.runCommand("freetime assd day/");
		assertResultMessage(invalidCommand);
		commandBox.runCommand("freetime asd");
		assertResultMessage(invalidCommand);
		commandBox.runCommand("freetime day/ 1 23 5 3");
		assertResultMessage(invalidArgs);
		commandBox.runCommand("freetime day/not a number");
		assertResultMessage(invalidArgs);
	}

}
