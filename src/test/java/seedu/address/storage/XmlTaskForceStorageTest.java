package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.TaskForce;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.Task;
import seedu.address.testutil.TypicalTestTasks;

public class XmlTaskForceStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskForceStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskForceNullFilePathAssertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readListData(null);
    }

    private java.util.Optional<ReadOnlyTaskForce> readListData(String filePath) throws Exception {
        return new XmlTaskForceStorage(filePath).readTaskForce(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
    }

    @Test
    public void read_missingFileEmptyResult() throws Exception {
        assertFalse(readListData("NonExistentFile.xml").isPresent());
    }

    @Test
    public void readNotXmlFormatExceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readListData("NotXmlFormatTaskForce.xml");

        /*
         * IMPORTANT: Any code below an exception-throwing line (like the one
         * above) will be ignored. That means you should not have more than one
         * exception test in one method
         */
    }

    @Test
    public void readAndSaveTaskForce_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempTaskForce.xml";
        TypicalTestTasks td = new TypicalTestTasks();
        TaskForce original = td.getTypicalTaskForce();
        XmlTaskForceStorage xmlTaskForceStorage = new XmlTaskForceStorage(filePath);

        // Save in new file and read back
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        ReadOnlyTaskForce readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTask(new Task(TypicalTestTasks.hoon));
        original.removeTask(new Task(TypicalTestTasks.alice));
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTask(new Deadline(0, TypicalTestTasks.hoon.getName(), TypicalTestTasks.hoon.getDescription(),
                LocalDateTime.now(), TypicalTestTasks.hoon.getTags()));
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        original.addTask(new Event(0, TypicalTestTasks.hoon.getName(), TypicalTestTasks.hoon.getDescription(),
                LocalDateTime.now(), LocalDateTime.now(), TypicalTestTasks.hoon.getTags()));
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        // Save and read without specifying file path
        original.addTask(new Task(TypicalTestTasks.ida));
        xmlTaskForceStorage.saveTaskForce(original); // file path not specified
        readBack = xmlTaskForceStorage.readTaskForce().get(); // file path not
                                                              // specified
        assertEquals(original, new TaskForce(readBack));

    }

    @Test
    public void saveTaskForceNullTaskForceAssertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskForce(null, "SomeFile.xml");
    }

    private void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException {
        new XmlTaskForceStorage(filePath).saveTaskForce(taskForce, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveTaskForceNullFilePathAssertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskForce(new TaskForce(), null);
    }

}
