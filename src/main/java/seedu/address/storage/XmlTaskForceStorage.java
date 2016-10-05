package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTaskForce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access TaskForce data stored as an xml file on the hard disk.
 */
public class XmlTaskForceStorage implements TaskForceStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskForceStorage.class);

    private String filePath;

    public XmlTaskForceStorage(String filePath){
        this.filePath = filePath;
    }

    public String getTaskForceFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readTaskForce()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskForce> readTaskForce(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File taskForceFile = new File(filePath);

        if (!taskForceFile.exists()) {
            logger.info("TaskForce file "  + taskForceFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskForce taskForceOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskForceOptional);
    }

    /**
     * Similar to {@link #saveTaskForce(ReadOnlyTaskForce)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException {
        assert taskForce != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskForce(taskForce));
    }

    @Override
    public Optional<ReadOnlyTaskForce> readTaskForce() throws DataConversionException, IOException {
        return readTaskForce(filePath);
    }

    @Override
    public void saveTaskForce(ReadOnlyTaskForce taskForce) throws IOException {
        saveTaskForce(taskForce, filePath);
    }
}
