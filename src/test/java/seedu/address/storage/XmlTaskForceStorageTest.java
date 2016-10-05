package seedu.address.storage;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.TaskForce;
import seedu.address.model.task.Task;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.testutil.TypicalTestTasks;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlTaskForceStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskForceStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskForce_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readTaskForce(null);
    }

    private java.util.Optional<ReadOnlyTaskForce> readTaskForce(String filePath) throws Exception {
        return new XmlTaskForceStorage(filePath).readTaskForce(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTaskForce("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readTaskForce("NotXmlFormatTaskForce.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveTaskForce_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempTaskForce.xml";
        TypicalTestTasks td = new TypicalTestTasks();
        TaskForce original = td.getTypicalTaskForce();
        XmlTaskForceStorage xmlTaskForceStorage = new XmlTaskForceStorage(filePath);

        //Save in new file and read back
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        ReadOnlyTaskForce readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTask(new Task(TypicalTestTasks.hoon));
        original.removeTask(new Task(TypicalTestTasks.alice));
        xmlTaskForceStorage.saveTaskForce(original, filePath);
        readBack = xmlTaskForceStorage.readTaskForce(filePath).get();
        assertEquals(original, new TaskForce(readBack));

        //Save and read without specifying file path
        original.addTask(new Task(TypicalTestTasks.ida));
        xmlTaskForceStorage.saveTaskForce(original); //file path not specified
        readBack = xmlTaskForceStorage.readTaskForce().get(); //file path not specified
        assertEquals(original, new TaskForce(readBack));

    }

    @Test
    public void saveTaskForce_nullTaskForce_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskForce(null, "SomeFile.xml");
    }

    private void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException {
        new XmlTaskForceStorage(filePath).saveTaskForce(taskForce, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveTaskForce_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskForce(new TaskForce(), null);
    }


}
