package guitests;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.testutil.TestTask;

//@@author A0139942W
public class FindCommandTest extends TaskForceGuiTest {
    private static final int MONDAY = 1;
    private static final int SUNDAY = 7;

    @Before
    public void setUp() {
        commandBox.runCommand("clear");
        commandBox.runCommand("add this is a task");
        commandBox.runCommand("add this is a task with description d/this is a description");
        commandBox.runCommand("add this is a task with tag t/important");
        commandBox.runCommand("add todayEvent st today 9pm et/today 9pm");
        commandBox.runCommand("add todayDeadline et/today 9pm");
        commandBox.runCommand("add lastWeekEvent st/last week 2pm et/last week 4pm");
        commandBox.runCommand("add lastWeekDeadline et/last week 2pm");
    }
    
    @Test
    public void findCommand_findByName_found() {
        commandBox.runCommand("find name/event");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 2));
        assertListSize(2);  
    }
    
    @Test
    public void findCommand_findByName_notFound() {
        commandBox.runCommand("find name/nonExistentTask");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0));
        assertListSize(0);  
    }
    
    
    @Test
    public void findCommand_findByDesc_found() {
        commandBox.runCommand("find desc/this");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
        assertListSize(1);   
    }
    
    public void findCommand_findByDesc_notFound() {
        commandBox.runCommand("find desc/cannotBeFound");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0));
        assertListSize(0);   
    }
    
    @Test
    public void findCommand_findByTag_found() {
        commandBox.runCommand("find tag/imp");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
        assertListSize(1);  
    }
    
    @Test
    public void findCommand_findByTag_notFound() {
        commandBox.runCommand("find tag/friend");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0));
        assertListSize(0);  
    }
    
    @Test
    public void findCommand_includeMarkFilter_found() {
        commandBox.runCommand("mark 1");
        commandBox.runCommand("find name/this ");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 2));
        assertListSize(2);  
        
        commandBox.runCommand("find name/this mark/true ");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
        assertListSize(3);  
    }

    
    /*
     * for find by day/week, floating test will always be displayed. Since we have 3 floating task,
     * the min size of the list using find day/ and find week/ should always be 3
     */
    @Test
    public void findCommand_findByDay_found() {
        commandBox.runCommand("find day/0");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 5));
        assertListSize(5);  
    }
    
    @Test
    public void findCommand_findByDay_notFound() {
        commandBox.runCommand("find day/-10");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
        assertListSize(3);  
    }
    
    @Test
    public void findCommand_findByWeek_found() {
        addMoreTask();
        commandBox.runCommand("find week/0");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 9));
        assertListSize(9);  
        
        commandBox.runCommand("find week/-1");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 5));
        assertListSize(5);  
    }
    
    @Test
    public void findCommand_findByWeek_notFound() {
        addMoreTask();
        commandBox.runCommand("find week/5");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 3));
        assertListSize(3);  
    }
    
    @Test
    public void findCommand_findByType_all_found() {
        commandBox.runCommand("find type/all");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 7));
        assertListSize(7);  
    }
    
    @Test
    public void findCommand_findByType_marked_found() {
        commandBox.runCommand("mark 1");
        commandBox.runCommand("mark 3");
        commandBox.runCommand("find type/mark");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 2));
        assertListSize(2); 
    }
    
    @Test
    public void findCommand_findByType_overdue_found() {
        commandBox.runCommand("find type/overdue");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 1));
        assertListSize(1); 
        commandBox.runCommand("mark 1");
        commandBox.runCommand("find type/overdue");
        assertResultMessage(String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0));
        assertListSize(0);
    }

    @Test
    public void findCommand_invalidCommand_showInvalidMessage() {
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
    
    private void addMoreTask() {
        LocalDateTime now = LocalDateTime.now();
        int dayOfTheWeek = now.getDayOfWeek().getValue();
        
        if (dayOfTheWeek == MONDAY) {
            commandBox.runCommand("add tomorrowDeadline et/tomorrow 6pm");
            commandBox.runCommand("add tomorrowEvent st/tomorrow 2pm et/tomorrow 6pm");
            commandBox.runCommand("add theDayAfterDeadline et/2 day later 6pm");
            commandBox.runCommand("add theDayAfterEvent st/2 day later 2pm et/2 day later 6pm");
            return;
        }
        
        if (dayOfTheWeek == SUNDAY) {
            commandBox.runCommand("add 2dayAgoDeadline et/2 day ago 6pm");
            commandBox.runCommand("add 2dayAgoEvent st/2 day ago 2pm et/2 day ago 6pm");
            commandBox.runCommand("add yesterdayDeadline et/ yesterday 6pm");
            commandBox.runCommand("add yesterdayEvent st/yesterday 2pm et/yesterday 6pm");
            return;
        }
        
        commandBox.runCommand("add tomorrowDeadline et/tomorrow 6pm");
        commandBox.runCommand("add tomorrowEvent st/tomorrow 2pm et/tomorrow 6pm");
        commandBox.runCommand("add yesterdayDeadline et/ yesterday 6pm");
        commandBox.runCommand("add yesterdayEvent st/yesterday 2pm et/yesterday 6pm");
        return;      
    }


// @@author A0135768R

    @Test
    public void findCommand_emptyList_noResult() {
        commandBox.runCommand("clear");
        assertFindResult("find name/Jean"); // no results
    }

    @Test
    public void findCommand_validCommand_pass() {

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

}
