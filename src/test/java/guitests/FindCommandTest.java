package guitests;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.testutil.TestTask;

//@@author A0139942W
public class FindCommandTest extends TaskForceGuiTest {

    @Before
    public void setUp() {
        commandBox.runCommand("clear");
    }

    @Test
    public void findInvalidCommandFail() {
        commandBox.runCommand("find day/123 sdf");
        assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
        commandBox.runCommand("find week/thisIsNotNumber");
        assertResultMessage(FindCommand.INVALID_FIND_DATE_MESSAGE);
        commandBox.runCommand("find all/");
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertResultMessage(expectedMessage);
        commandBox.runCommand("find asdf day/123");
        assertResultMessage(expectedMessage);
        commandBox.runCommand("find type/asd");
        assertResultMessage(FindCommand.INVALID_FIND_TYPE_MESSAGE);

    }

    @Test
    public void validCommandFindName() {
        commandBox.runCommand("add this is a task");
        commandBox.runCommand("add this is a event st today 9pm et/today 9pm");
        commandBox.runCommand("add this is a deadline et/today 9pm");
        commandBox.runCommand("find name/this is");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));

    }

    @Test
    public void validCommandFindTag() {
        commandBox.runCommand("add task1 this has a tag t/important");
        commandBox.runCommand("add task2 this has no tag ");
        commandBox.runCommand("add task3 this has no tag 2 ");
        commandBox.runCommand("find tag/imp");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
    }

    @Test
    public void validCommandFindDesc() {
        commandBox.runCommand("add a task d/this has description");
        commandBox.runCommand("find desc/this has description");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
    }

    @Test
    public void validCommandFindMarkedTasked() {
        commandBox.runCommand("add a task that is marked");
        commandBox.runCommand("mark 1");
        commandBox.runCommand("find name/task");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0));
        commandBox.runCommand("find name/task mark/true");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
    }

    @Test
    public void validCommandFindDay() {
        commandBox.runCommand("add a event st/today 12pm et/today 5pm");
        commandBox.runCommand("add a task ");
        commandBox.runCommand("add a deadline et/today 2pm");
        commandBox.runCommand("add notRelevantEvent st/last week 2pm et/last week 4pm");
        commandBox.runCommand("find day/0");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
    }

    @Test
    public void validCommandFindWeek() {
        commandBox.runCommand("add nextWeekEvent st/next week 2pm et/9 day later 5pm");
        commandBox.runCommand("add nextWeekDeadline et/next week 5pm ");
        commandBox.runCommand("add this is a normal task");
        commandBox.runCommand("add Not Relevant event st/today 2pm et/today 6pm");
        commandBox.runCommand("find week/1");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
    }

    @Test
    public void validCommandFindType() {
        commandBox.runCommand("add overdueDeadline et/last week 2pm");
        commandBox.runCommand("add floatingTask");
        commandBox.runCommand("add LastmarkedTask et/today 5pm");
        commandBox.runCommand("mark 2");
        commandBox.runCommand("find type/all");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
        commandBox.runCommand("find type/mark");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
        commandBox.runCommand("find type/overdue");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
    }

// @@author A0135768R

    @Test
    public void findEmptyList() {
        commandBox.runCommand("clear");
        assertFindResult("find name/Jean"); // no results
    }

    @Test
    public void findMinimalValidCommand_pass() {

        commandBox.runCommand("clear");

        List<TestTask> list = populateTestData();

        for (TestTask task : list) {
            StringBuilder sb = new StringBuilder();

            sb.append("add " + task.getName());

            if (task.getEndDate() != null) {
                sb.append(" et/" + task.getEndDate().toString());
            }

            if (task.getStartDate() != null) {
                sb.append(" st/" + task.getStartDate().toString());
            }

            commandBox.runCommand(sb.toString());
        }

        TestTask[] array = new TestTask[list.size()];

        assertFindResult("find name/john", list.toArray(array));

        assertFindResult("find day/0", list.get(0), list.get(1), list.get(2));
        assertFindResult("find day/1", list.get(3));
        assertFindResult("find week/1",
                (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) ? list.get(3) : list.get(4));

    }

    private void assertFindResult(String command, TestTask... expectedHits) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }

    private List<TestTask> populateTestData() {
        List<TestTask> list = Lists.newLinkedList();

        for (int i = 0; i < 5; i++) {
            TestTask task = new TestTask();
            task.setName("john " + i);

            if (i < 3) {
                task.setEndDate(DateUtil.END_OF_TODAY);

            } else if (i >= 3 && i < 4) {
                task.setEndDate(DateUtil.parseStringIntoDateTime("tomorrow").get());

            } else {

                String dateString = "next monday";

                task.setEndDate(DateUtil.parseStringIntoDateTime(dateString).get());
            }

            list.add(task);
        }

        return list;
    }

    @After
    public void clear() {
        commandBox.runCommand("clear");
    }
}
