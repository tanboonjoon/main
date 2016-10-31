package seedu.address.storage;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskForce;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for {@link seedu.address.model.TaskForce}.
 */
public interface TaskForceStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskForceFilePath();

    /**
     * Returns TaskForce data as a {@link ReadOnlyTaskForce}. Returns
     * {@code Optional.empty()} if storage file is not found.
     * 
     * @throws DataConversionException
     *             if the data in storage is not in the expected format.
     * @throws IOException
     *             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskForce> readTaskForce() throws DataConversionException, IOException;

    /**
     * @see #getTaskForceFilePath()
     */
    Optional<ReadOnlyTaskForce> readTaskForce(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskForce} to the storage.
     * 
     * @param taskForce
     *            cannot be null.
     * @throws IOException
     *             if there was any problem writing to the file.
     */
    void saveTaskForce(ReadOnlyTaskForce taskForce) throws IOException;

    /**
     * @see #saveTaskForce(ReadOnlyTaskForce)
     */
    void saveTaskForce(ReadOnlyTaskForce taskForce, String filePath) throws IOException;

}
