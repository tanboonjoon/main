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

        File addressBookFile = new File(filePath);

        if (!addressBookFile.exists()) {
            logger.info("TaskForce file "  + addressBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskForce addressBookOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(addressBookOptional);
    }

    /**
     * Similar to {@link #saveTaskForce(ReadOnlyTaskForce)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskForce(ReadOnlyTaskForce taskforce, String filePath) throws IOException {
        assert taskforce != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskForce(taskforce));
    }

    @Override
    public Optional<ReadOnlyTaskForce> readTaskForce() throws DataConversionException, IOException {
        return readTaskForce(filePath);
    }

    @Override
    public void saveTaskForce(ReadOnlyTaskForce taskforce) throws IOException {
        saveTaskForce(taskforce, filePath);
    }
}
