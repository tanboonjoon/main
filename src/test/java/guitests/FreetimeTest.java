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
	
	private LocalDateTime yesterday;
	private LocalDateTime now;
	private LocalDateTime tomorrow;
	
	private DateTimeFormatter addCommandFormatter;
	private DateTimeFormatter ongoingEventFormatter;
	private DateTimeFormatter eventFormatter;
	@Before 
	public void clearList() {
		commandBox.runCommand("clear");
	}

	
	@Before
	public void setUp() {


		addCommandFormatter = DateTimeFormatter.ofPattern("MM-dd-yyy HHmm");
		ongoingEventFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HHmm");
		eventFormatter =  DateTimeFormatter.ofPattern("dd/MM/yyyy");

		now = LocalDateTime.now();
		yesterday = roundUpTime(now.minusDays(1));
		tomorrow = roundUpTime(now.plusDays(1));
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
		commandBox.runCommand("add event st/today 3pm et/today 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1500"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1700"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void validCommandOneOngoingEvent() {
		

		commandBox.runCommand("add event st/" + yesterday.format(addCommandFormatter) + " et/" + tomorrow.format(addCommandFormatter));
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.ONGOING_EVENT_MESSAGE, yesterday.format(ongoingEventFormatter), tomorrow.format(ongoingEventFormatter)));
		assertResultMessage(sb.toString());
	}
	
	
	
	@Test
	public void validCommandOneOngoingEndTodayEvent() {
		commandBox.runCommand("add event st/yesterday 3pm et/today 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1700"));
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void validCommandOneOngoingStartTodayEvent() {

		commandBox.runCommand("add event st/today 3pm et/tomorrow 5pm");
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "1500"));
		assertResultMessage(sb.toString());
	}

	@Test
	public void validCommandMultipleEvent() {
		commandBox.runCommand("add event st/today 3am et/today 12pm");
		commandBox.runCommand("add event2 st/today 2:30am et/today 11am");
		commandBox.runCommand("add event3 st/today 2pm et/today 5pm");
		commandBox.runCommand("freetime day/0");
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.FIRST_EVENT_MESSAGE, "0230"))
		.append(String.format(FreetimeCommand.BETWEEN_EVENT_MESSAGE, "1200", "1400"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1700"));
		
		assertResultMessage(sb.toString());
	}
	
	@Test
	public void validCommandLongEventWithManyEvent() {
		commandBox.runCommand("add event st/today 3am et/today 12pm");
		commandBox.runCommand("add event2 st/today 2:30am et/today 11pm");
		commandBox.runCommand("add event st/" + yesterday.format(addCommandFormatter) + " et/" + tomorrow.format(addCommandFormatter));
		StringBuilder sb = new StringBuilder();
		commandBox.runCommand("freetime day/0");
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.ONGOING_EVENT_MESSAGE, yesterday.format(ongoingEventFormatter), tomorrow.format(ongoingEventFormatter)));
		assertResultMessage(sb.toString());
		
	}
	
	@Test
	public void validCommandMutipleEventEndLater() {
		commandBox.runCommand("add event st/today 2:30am et/today 11am");
		commandBox.runCommand("add event2 st/" + yesterday.format(addCommandFormatter) + " et/today 12pm");
		commandBox.runCommand("add event3 st/today 5pm et/today 6pm");
		commandBox.runCommand("freetime day/0");
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
		.append(String.format(FreetimeCommand.BETWEEN_EVENT_MESSAGE, "1200", "1700"))
		.append(String.format(FreetimeCommand.LAST_EVENT_MESSAGE, "1800"));
		assertResultMessage(sb.toString());
	}
		
		
	

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
