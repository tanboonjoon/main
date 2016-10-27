package seedu.address.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.TaskForce;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalTestTasks;
import seedu.address.testutil.EventsCollector;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setUp() {
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskForceReadSave() throws Exception {
        TaskForce original = new TypicalTestTasks().getTypicalTaskForce();
        storageManager.saveTaskForce(original);
        ReadOnlyTaskForce retrieved = storageManager.readTaskForce().get();
        assertEquals(original, new TaskForce(retrieved));
        //More extensive testing of TaskForce saving/reading is done in XmlTaskForceStorageTest
    }

    @Test
    public void getTaskForceFilePath(){
        assertNotNull(storageManager.getTaskForceFilePath());
    }

    @Test
    public void handleTaskForceChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskForceStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskForceChangedEvent(new TaskForceChangedEvent(new TaskForce()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskForceStorageExceptionThrowingStub extends XmlTaskForceStorage{

        public XmlTaskForceStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
