package guitests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import seedu.address.logic.commands.FreetimeCommand;

//@@author A0139942W
/*
 * Testing of FreetimeCommand, Due to the nature of freetiemCommand,
 * Each test stimulate different sets of event/deadline of different timing.
 * 
 */
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
    public void setUp() {
        commandBox.runCommand("clear");
        addCommandFormatter = DateTimeFormatter.ofPattern("MM-dd-yyy HHmm");
        ongoingEventFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HHmm");
        eventFormatter =  DateTimeFormatter.ofPattern("dd/MM/yyyy");
        now = LocalDateTime.now();
        yesterday = roundUpTime(now.minusDays(1));
        tomorrow = roundUpTime(now.plusDays(1));
    }


    @Test
    public void freetimeCommand_invalidCommand_displayInvalidMessage() {
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
    public void freetimeCommand_noEvent_displayNoEventMessage() {
        commandBox.runCommand("add floatingTask");
        commandBox.runCommand("freetime day/0");
        assertResultMessage(FreetimeCommand.ZERO_EVENT_MESSAGE);
    }



    @Test
    public void freetimeCommand_oneEvent_displayHasFreeSlot() {
        commandBox.runCommand("add event st/today 3pm et/today 5pm");
        StringBuilder sb = new StringBuilder();
        commandBox.runCommand("freetime day/0");
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.NUM_OF_FREESLOT_MESSAGE, 2));
        assertResultMessage(sb.toString());
    }
/*
    @Test
    public void freetimeCommand_oneOngoingEvent_displayHasNoFreeSlot() {
        commandBox.runCommand("add event st/" + yesterday.format(addCommandFormatter) + " et/" + tomorrow.format(addCommandFormatter));
        StringBuilder sb = new StringBuilder();
        commandBox.runCommand("freetime day/0");
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.ONGOING_EVENT_MESSAGE, yesterday.format(ongoingEventFormatter), tomorrow.format(ongoingEventFormatter)));
        assertResultMessage(sb.toString());
    }
*/


    @Test
    public void freetimeCommand_oneEventStartEndOutsideActive_displayHasFreeSlot() {
        commandBox.runCommand("add event st/yesterday 7am et/today 5pm");
        StringBuilder sb = new StringBuilder();
        commandBox.runCommand("freetime day/0");
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.NUM_OF_FREESLOT_MESSAGE, 1));
        assertResultMessage(sb.toString());
        commandBox.runCommand("delete 1");
        commandBox.runCommand("add event st/today 9am et/tomorrow 11pm");
        commandBox.runCommand("freetime day/0");
        assertResultMessage(sb.toString());
    }


/*
    @Test
    public void freetimeCommand_oneEvent_displayHasNoFreeSlot() {
        commandBox.runCommand("add event st/6am et/11pm");
        commandBox.runCommand("freetime day/0");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(FreetimeCommand.NO_FREE_TIME_MESSAGE);
        assertResultMessage(sb.toString());
    }
    */

    @Test
    public void freetimeCommand_mutipleEvent_displayHasFreeSlot() {
        commandBox.runCommand("add event st/today 9am et/today 12pm");
        commandBox.runCommand("add event2 st/today 10:30am et/today 11am");
        commandBox.runCommand("add event3 st/today 2pm et/today 5pm");
        commandBox.runCommand("freetime day/0");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.NUM_OF_FREESLOT_MESSAGE, 3));
        assertResultMessage(sb.toString());
    }
/*
    @Test
    public void freetimeCommand_mutipleLongEvent_displayOngoingEvent() {
        commandBox.runCommand("add event st/today 3am et/today 12pm");
        commandBox.runCommand("add event2 st/today 2:30am et/today 11pm");
        commandBox.runCommand("add event st/" + yesterday.format(addCommandFormatter) + " et/" + tomorrow.format(addCommandFormatter));
        StringBuilder sb = new StringBuilder();
        commandBox.runCommand("freetime day/0");
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.ONGOING_EVENT_MESSAGE, yesterday.format(ongoingEventFormatter), tomorrow.format(ongoingEventFormatter)));
        assertResultMessage(sb.toString());
    }
*/
    @Test
    public void freetimeCommand_mutipleEventEndAfterActive_displayHasFreeSlot() {
        commandBox.runCommand("add event st/today 9:30am et/today 11am");
        commandBox.runCommand("add event2 st/" + yesterday.format(addCommandFormatter) + " et/today 12pm");
        commandBox.runCommand("add event3 st/today 5pm et/today 10pm");
        commandBox.runCommand("freetime day/0");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.NUM_OF_FREESLOT_MESSAGE, 1));
        assertResultMessage(sb.toString());
    }

    @Test
    public void freetimeCommand_mutipleEventStartBeforeActive_displayHasFreeSlot() {
        commandBox.runCommand("add event st/today 2:30am et/today 11am");
        commandBox.runCommand("add event2 st/" + yesterday.format(addCommandFormatter) + " et/today 12pm");
        commandBox.runCommand("add event3 st/today 5pm et/today 6pm");
        commandBox.runCommand("freetime day/0");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FreetimeCommand.DEFAULT_STARTING_MESSAGE, now.format(eventFormatter)))
        .append(String.format(FreetimeCommand.NUM_OF_FREESLOT_MESSAGE, 2));
        assertResultMessage(sb.toString());
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
