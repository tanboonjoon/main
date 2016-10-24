package guitests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import seedu.address.logic.commands.FreetimeCommand;

//@@author A0139942W
public class FreetimeTest extends TaskForceGuiTest{
	private static final int HALF_AN_HOUR = 30;
	private static final int AN_HOUR = 60;
	private static final int EXACT_AN_HOUR = 00;
	
	@Before 
	public void clearList() {
		commandBox.runCommand("clear");
	}
	@Test
	public void invalidCommand() {
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
	public void validCommnadNoEvent() {
		commandBox.runCommand("add floatingTask");
		commandBox.runCommand("freetime day/0");
		assertResultMessage(FreetimeCommand.ZERO_EVENT_MESSAGE);
	}
	
	
	
	@Test
	public void validCommandOneEvent() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		commandBox.runCommand("add event st/today 3pm et/today 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(formatter)))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1500"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1700"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void validCommandOneOngoingEvent() {
		DateTimeFormatter addFormat = DateTimeFormatter.ofPattern("MM-dd-yyy HHmm");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime yestarday = roundUpTime(now.minusDays(1));
		LocalDateTime tomorrow = roundUpTime(now.plusDays(1));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter ongoingFormat = DateTimeFormatter.ofPattern("dd/MM/yyy HHmm");
		commandBox.runCommand("add event st/" + yestarday.format(addFormat) + " et/" + tomorrow.format(addFormat));
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(formatter)))
		.append(String.format(FreetimeCommand.ONGOING_EVENT_MESSAGE, yestarday.format(ongoingFormat), tomorrow.format(ongoingFormat)));
		assertResultMessage(sb.toString());
	}
	
	
	
	@Test
	public void validCommandOneOngoingEndTodayEvent() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		commandBox.runCommand("add event st/yesterday 3pm et/today 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(formatter)))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1700"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void validCommandOneOngoingStartTodayEvent() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		commandBox.runCommand("add event st/today 3pm et/tomorrow 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(formatter)))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1500"));
		assertResultMessage(sb.toString());
	}
	
	/*
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
		commandBox.runCommand("add event st/10-21-2016 1330 et/10-21-2016 1340");
		commandBox.runCommand("add event2 st/10-21-2016 1500 et/10-21-2016 1700");
		commandBox.runCommand("add event3 st/10-21-2016 1520 et/10-21-2016 1730 ");
		commandBox.runCommand("mark 4");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/1");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE,"21/10/2016"))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1300"))
		.append(String.format(FreetimeCommand.BETWEEN_EVENT_MESSAGE, "1330", "1500"))
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
	*/
	@After
	public void clear() {
		commandBox.runCommand("clear");
	}
	
	private LocalDateTime roundUpTime(LocalDateTime dateTime) {
		int minutes = dateTime.getMinute();
		if (minutes == EXACT_AN_HOUR) {
			return dateTime;
		}
		if (minutes <= HALF_AN_HOUR) {
			System.out.println(dateTime.toString());
			return dateTime.plusMinutes(HALF_AN_HOUR - minutes);
		}

		return dateTime.plusMinutes(AN_HOUR - minutes);
	
	}
	

}
