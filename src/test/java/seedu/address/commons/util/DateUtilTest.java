package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javafx.util.Pair;
import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.TaskForce;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Event;
import seedu.address.storage.StorageManager;

// @@author A0135768R
public class DateUtilTest {

    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskForce latestSavedTaskForce;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Before
    public void setTestEnvironment () {
        model = new ModelManager () ;
        String tempAddressBookFile = saveFolder.getRoot().getPath() + "TempAddressBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempAddressBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskForce = new TaskForce(model.getTaskForce()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @Test
    public void testParseStringIntoDateTime () {

        LocalDateTime expected ;

        // EP: empty strings
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime(""), true, null) ;
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime(" "), true, null) ;

        // EP: nonsense string
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime("aaaa"), true, null) ;
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime(" dsd"), true, null) ;

        // EP: null
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime(null), true, null) ;

        // EP: Only numbers provided
        expected = LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0) ;        
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime("15"), false, expected) ;

        expected = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0) ;  
        DateUtilTest.<LocalDateTime>assertOptional(DateUtil.parseStringIntoDateTime("00000"), false, expected) ;


    }

    @Test
    public void testCheckForConflictingEvents () throws Exception {
        Pair<LocalDateTime, LocalDateTime> pair ;

        // Add some sample events
        pair = getStartAndEndDates("today 5pm", "today 7pm") ;
        Event event1 = new Event (0, "event1", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(event1);

        pair = getStartAndEndDates("tomorrow 3pm", "tomorrow 7pm") ;
        Event event2 = new Event (0, "event2", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(event2);

        pair = getStartAndEndDates("today 1pm", "today 2pm") ;
        Event event3 = new Event (0, "event3", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(event3);

        Event eventToBeAdded ;

        // EP: Start and end dates are the same
        pair = getStartAndEndDates("today 5pm", "today 7pm") ;
        eventToBeAdded = new Event (0, "event4", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(eventToBeAdded);

        DateUtilTest.<Event>assertOptional(DateUtil.checkForConflictingEvents(model, eventToBeAdded), false, event1 ) ;

        // EP: Start and end dates are in between existing events
        pair = getStartAndEndDates("today 1:30pm", "today 1:59pm") ;
        eventToBeAdded = new Event (0, "event5", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(eventToBeAdded);

        DateUtilTest.<Event>assertOptional(DateUtil.checkForConflictingEvents(model, eventToBeAdded), false, event3 ) ;

        // EP: A long event
        pair = getStartAndEndDates("today 7am", "today 11pm") ;
        eventToBeAdded = new Event (0, "event6", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(eventToBeAdded);

        DateUtilTest.<Event>assertOptional(DateUtil.checkForConflictingEvents(model, eventToBeAdded), false, event1 ) ;

        // EP: Event started and end before other events
        pair = getStartAndEndDates("tomorrow 7am", "tomorrow 9am") ;
        eventToBeAdded = new Event (0, "event7", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(eventToBeAdded);

        DateUtilTest.<Event>assertOptional(DateUtil.checkForConflictingEvents(model, eventToBeAdded), true, null ) ;

        // EP: Event started and end after other events
        pair = getStartAndEndDates("tomorrow 7:01pm", "tomorrow 9pm") ;
        eventToBeAdded = new Event (0, "event8", "", pair.getKey(), pair.getValue(), new UniqueTagList()) ;
        model.addTask(eventToBeAdded);

        DateUtilTest.<Event>assertOptional(DateUtil.checkForConflictingEvents(model, eventToBeAdded), true, null ) ;
    }


    /**
     * Given a optional value, assert that the optional is empty is isEmpty is set to true,
     * or if set to false, asserts that the value contained within the optional value is the given value.
     * 
     * @param isEmpty   whether the Optional should be empty.
     * @param value     If optional is not empty, asserts that the value contained within is this value
     */
    private static <T> void assertOptional (Optional<T> optional, boolean isEmpty, T value) {

        if (isEmpty) {
            assertFalse(optional.isPresent()) ;
        } else {        
            assertTrue (optional.get().equals(value)) ;
        }
    }

    private static Pair<LocalDateTime, LocalDateTime> getStartAndEndDates (String start, String end) {
        return new Pair<LocalDateTime, LocalDateTime> (DateUtil.parseStringIntoDateTime(start).get(), DateUtil.parseStringIntoDateTime(end).get() ) ;
    }

}
