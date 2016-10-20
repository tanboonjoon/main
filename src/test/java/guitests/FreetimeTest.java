package guitests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FreetimeCommand;

//@@ A0139942W
public class FreetimeTest extends TaskForceGuiTest{
	
	@Before 
	public void clearList() {
		commandBox.runCommand("clear");
	}
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
	
	@Test
	public void valid_commnad_no_event() {
		commandBox.runCommand("add floatingTask");
		commandBox.runCommand("freetime day/0");
		assertResultMessage(FreetimeCommand.ZERO_EVENT_MESSAGE);
	}
	
	
	@Test
	public void valid_command_one_event() {
		commandBox.runCommand("add event st/10-20-2016 1700 et/10-20-2016 2100");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE,"20/10/2016"))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1700"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "2100"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void valid_command_one_long_event() {
		commandBox.runCommand("add event st/10-21-2016 1700 et/10-22-2016 2100");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/1");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE,"21/10/2016"))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1700"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void valid_command_multiple_event() {
		commandBox.runCommand("add event st/10-21-2016 1300 et/10-21-2016 1400");
		commandBox.runCommand("add event2 st/10-21-2016 1500 et/10-21-2016 1700");
		
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/1");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE,"21/10/2016"))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1300"))
		.append(String.format(FreetimeCommand.BETWEEN_EVENT_MESSAGE, "1400", "1500"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE,  "1700"));
		assertResultMessage(sb.toString());
		
	}
	
	@Test	
	public void valid_command_mutiple_event_long() {
		commandBox.runCommand("add event st/10-21-2016 1300 et/10-21-2016 1400");
		commandBox.runCommand("add event2 st/10-21-2016 1500 et/10-22-2016 1700");
		
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/1");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE,"21/10/2016"))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1300"))
		.append(String.format(FreetimeCommand.BETWEEN_EVENT_MESSAGE, "1400", "1500"));
		assertResultMessage(sb.toString());
	}
	
	@After
	public void clear() {
		commandBox.runCommand("clear");
	}
	

}
