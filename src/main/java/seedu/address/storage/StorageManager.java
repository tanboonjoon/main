package seedu.address.storage;

import com.google.common.eventbus.Subscribe;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskForceChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskForce;
import seedu.address.model.UserPrefs;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of TaskForce data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskForceStorage taskForceStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskForceStorage taskForceStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskForceStorage = taskForceStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String taskForceFilePath, String userPrefsFilePath) {
        this(new XmlTaskForceStorage(taskForceFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskForce methods ==============================

    @Override
    public String getTaskForceFilePath() {
        return taskForceStorage.getTaskForceFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskForce> readTaskForce() throws DataConversionException, IOException {
        return readTaskForce(taskForceStorage.getTaskForceFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskForce> readTaskForce(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskForceStorage.readTaskForce(filePath);
    }

    @Override
    public void saveTaskForce(ReadOnlyTaskForce taskForce) throws IOException {
        saveTaskForce(taskForce, taskForceStorage.getTaskForceFilePath());
    }

    @Override
    public void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskForceStorage.saveTaskForce(taskForce, filePath);
    }
    
    
    public void setTaskForceDirectory(String newTaskForcePath) {
        this.taskForceStorage = new XmlTaskForceStorage(newTaskForcePath);

    }


    @Override
    @Subscribe
    public void handleTaskForceChangedEvent(TaskForceChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskForce(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }



}
