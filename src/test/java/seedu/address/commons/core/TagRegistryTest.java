package seedu.address.commons.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistrar;

// @@author A0135768R
public class TagRegistryTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none() ;
    
    private static ImmutableList<Tag> TAG_LIST ;
    private static TagRegistrar tagRegistry ;
    
    @Before
    public void setupVariables () throws Exception {
        Builder<Tag> builder = ImmutableList.builder() ;
        
        builder
        .add(new Tag("tag1"))
        .add(new Tag("tag2"))
        .add(new Tag("tag3")) ;
        
        TAG_LIST = builder.build() ;
        
        tagRegistry = new TagRegistrar() ;
        tagRegistry.setAllTags(TAG_LIST);
    }
    
    @Test
    public void tagRegistryTestInvalidValues() throws Exception {

        // EP: NULL values
        thrown.expect(AssertionError.class) ;
        tagRegistry.getTagFromString(null, true) ;
        
        // EP: Empty values
        thrown.expect(IllegalValueException.class) ;
        tagRegistry.getTagFromString("", true) ;
        
        // EP: null and empty values, but not registered
        assertTrue (tagRegistry.getTagFromString("", false) == null) ;
        assertTrue (tagRegistry.getTagFromString(null, false) == null) ;
    }
    
    @Test
    public void tagRegistryExistingValues() throws Exception {
        assertTrue (tagRegistry.getTagFromString("tag1", true) == TAG_LIST.get(0)) ;
    }
    
    @Test
    public void tagRegistryEditValues() throws Exception {
        Tag tag2 = TAG_LIST.get(1) ;
        
        tagRegistry.changeTagName(tag2, "2tag");
        assertTrue (tagRegistry.getTagFromString("2tag", true) == TAG_LIST.get(1)) ;
    }
}
