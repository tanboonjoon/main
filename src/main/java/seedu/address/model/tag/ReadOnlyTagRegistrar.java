package seedu.address.model.tag;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * @@author A0135768R
 * 
 *          A tag registry that disallows classes to reset the whole registry.
 */
public interface ReadOnlyTagRegistrar {

    public static final String MESSAGE_DUPLICATE_NAME = "The given tag name already exists!";

    /**
     * Given a tag string to the registry, the registry will return the Tag
     * object with tag name matching the given string.
     * <p>
     * Additionally if register is set to true, if the tag is not currently in
     * the registry, this create a new Tag object with the given string and
     * returns it. <br>
     * 
     * If not, this function returns null ;
     * 
     * @param tagString
     *            The tag name to be queried
     * @param register
     *            If true, this will register a new tag with the given name if
     *            not found; otherwise this will return null;
     * @return A tag object with the given tag name
     * @throws IllegalValueException
     *             if the given tag string is illegal and/or invalid
     */
    public Tag getTagFromString(String tagString, boolean register) throws IllegalValueException;

    /**
     * Given an existing tag, change its name to the given string
     * 
     * @param tag
     *            The tag object whose name is to be modified
     * @param newName
     *            the name to change into
     * 
     * @throws IllegalValueException
     *             if the given name is invalid and/or already exists.
     */
    public void changeTagName(Tag tag, String newName) throws IllegalValueException;

}
