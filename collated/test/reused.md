# A0135768R-reused
###### \java\seedu\address\commons\util\StringUtilTest.java
``` java
    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCaseNullWordExceptionThrown(){
        assertExceptionThrown("typical sentence", null, "Word parameter cannot be null");
    }

    private void assertExceptionThrown(String sentence, String word, String errorMessage) {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(errorMessage);
        StringUtil.containsWordIgnoreCase(sentence, word);
    }

    @Test
    public void containsWordIgnoreCaseEmptyWordExceptionThrown(){
        assertExceptionThrown("typical sentence", "  ", "Word parameter cannot be empty");
    }

    @Test
    public void containsWordIgnoreCaseMultipleWordsExceptionThrown(){
        assertExceptionThrown("typical sentence", "aaa BBB", "Word parameter should be a single word");
    }

    @Test
    public void containsWordIgnoreCaseNullSentenceExceptionThrown(){
        assertExceptionThrown(null, "abc", "Sentence parameter cannot be null");
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCaseValidInputsCorrectResult(){

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetailsExceptionGiven(){
        assertThat(StringUtil.getDetails(new FileNotFoundException("file not found")),
                containsString("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetailsNullGivenAssertionError(){
        thrown.expect(AssertionError.class);
        StringUtil.getDetails(null);
        
        assert true ;
    }


}
```
