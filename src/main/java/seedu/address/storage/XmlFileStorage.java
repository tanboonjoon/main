package seedu.address.storage;

import seedu.address.commons.util.XmlUtil;
import seedu.address.commons.exceptions.DataConversionException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stores taskForce data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given taskForce data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableTaskForce taskForce)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, taskForce);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableTaskForce loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableTaskForce.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
