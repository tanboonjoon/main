package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.TaskForce;
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
    
}
