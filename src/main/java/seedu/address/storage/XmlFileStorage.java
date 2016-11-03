package seedu.address.storage;

import seedu.address.commons.util.XmlUtil;
import seedu.address.commons.exceptions.DataConversionException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stores task force data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given Task Force data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableTaskForce taskforce) throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, taskforce);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns Task Force in the file or an empty Task Force
     */
    public static XmlSerializableTaskForce loadDataFromSaveFile(File file)
            throws DataConversionException, FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableTaskForce.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
