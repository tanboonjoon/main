package seedu.address.model.tag;

import java.util.Map;

import com.google.common.collect.Maps;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * @@author A0135768R
 * 
 * <p>
 * 
 * Provides a central class to handle tag creation to prevent duplicate tags with similar names
 *
 */
public class TagRegistrar implements ReadOnlyTagRegistrar {
    
    private final Map<String, Tag> tagRegistry = Maps.newHashMap() ;
    
    @Override
    public Tag getTagFromString (String tagString, boolean register) throws IllegalValueException {
        
        String key = parseInput(tagString) ;
        
        if (tagRegistry.containsKey(key)) {
            return tagRegistry.get(key) ;
        }
        
        return (register) ? registerNewTag(key) : null ;
    }
    
    private Tag registerNewTag (String tagString) throws IllegalValueException {
        String newTagString = parseInput(tagString) ;
        Tag newTag = new Tag(newTagString) ;
        
        tagRegistry.put(newTagString, newTag) ;
        
        return newTag ;
    }
    
    private String parseInput (String input) {
        return input.trim().toLowerCase() ;
    }
    
    /**
     * Resets the registry and re-populate it with the given tags
     * @param tagIterable
     */
    public void setAllTags (Iterable<Tag> tagIterable) {
        tagRegistry.clear() ;
        
        for (Tag tag : tagIterable) {
            tagRegistry.put(parseInput(tag.tagName), tag) ;
        }
    }
    
    public void changeTagName(Tag tag, String newName) throws IllegalValueException {
        String name = parseInput(newName) ;
        String curName = tag.tagName ;
        
        if (tagRegistry.containsKey(name)) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_NAME) ;
        }
        
        tag.setTagName(name) ;
        
        tagRegistry.remove(curName) ;
        tagRegistry.put(name, tag) ;
    }
}
