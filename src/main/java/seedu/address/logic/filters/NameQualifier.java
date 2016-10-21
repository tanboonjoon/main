package seedu.address.logic.filters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;


/**
 * @@author A0139942W
 * 
 * A general qualifier used by the find command to filter tasks according to user's input
 *
 */
public class NameQualifier implements Qualifier {

    private Set<String> nameKeyWords;
    private String findType;
    private DateTimeFormatter format_exclude_time;
    
    private final String SEARCH_NAME = "NAME";
    private final String SEARCH_DESC = "DESC";
    private final String SEARCH_TAG = "TAG";
    
    private final boolean TASK_NOT_FOUND = false;
    private final boolean TASK_FOUND = true;

    private final int STARTING_INDEX  = 0;
    private final int LAST_DAY_INDEX = 7;

    private final int SAME_DAY_VALUE = 0;
    private final int AFTER_START_DATE = 0;
    private final int BEFORE_END_DATE = 0;
    private final int DATE_ARGS_INDEX = 0;
    private final int FORMATTED_DATE_INDEX = 0;
    private final int GET_TO_MONDAY_INDEX = 1;

    private ArrayList<String> formattedDateList;
    private ArrayList<LocalDateTime> dateToCompareList;

    public NameQualifier(Set<String> nameKeyWords, String findType) {

        this.formattedDateList = new ArrayList<String>();
        this.dateToCompareList = new ArrayList<LocalDateTime>();
        this.format_exclude_time = DateTimeFormatter.ofPattern("ddMMyyyy");
        this.nameKeyWords = nameKeyWords;
        this.findType = findType;
    }

    @Override
    public boolean run(ReadOnlyTask task) {

        if (isKeywordSearch(findType)) {
            Trie keywordTrie = buildKeyword();
            return filterByKeyWord(task, keywordTrie, findType);
        }

        getDateForCompare();
        getFormattedDate();

        if (task instanceof Deadline) {
            String taskEndDate = (((Deadline) task).getEndDate()).format(format_exclude_time);
            return filterDeadLine(taskEndDate);
        }

        if (task instanceof Event) {
            String taskStartDate = ((Event) task).getStartDate().format(format_exclude_time);
            String taskEndDate = ((Event) task).getEndDate().format(format_exclude_time);
            return filterEvent(taskStartDate, taskEndDate);
        }

        return TASK_FOUND;

    }
    
    private boolean isKeywordSearch(String searchType) {
    	return  searchType.equals(SEARCH_NAME) ||
    			searchType.equals(SEARCH_TAG) ||
    			searchType.equals(SEARCH_DESC);
    }

    private Trie buildKeyword() {

        List<String> keywordList = new ArrayList<String>(nameKeyWords);
        TrieBuilder trie = Trie.builder();
        for (int keyword_index = STARTING_INDEX; keyword_index < keywordList.size(); keyword_index++) {
            String keyword = keywordList.get(keyword_index);
            trie.addKeyword(keyword);

        }
        return trie.caseInsensitive().removeOverlaps().build();
    }

    private boolean filterByKeyWord(ReadOnlyTask task, Trie keywordTrie, String searchType) {
    	if (searchType.equals(SEARCH_NAME)) {
    		return keywordTrie.containsMatch(task.getName());
    	}
    	
    	if (searchType.equals(SEARCH_DESC)) {
    		return keywordTrie.containsMatch(task.getDescription());
    	}
    	
    	return filterByTag(task, keywordTrie);
    }

    private boolean filterByTag(ReadOnlyTask task, Trie keywordTrie) {
		// TODO Auto-generated method stub
		UniqueTagList tagList = task.getTags();
		
		for	(Tag tag : tagList) {
			if (keywordTrie.containsMatch(tag.tagName)) {
				return TASK_FOUND;
			}
		}
		
		return TASK_NOT_FOUND;
	}

	public boolean filterDeadLine(String taskStartDate) {

        if (findType.equals("DAY")) {
            return formattedDateList.get(FORMATTED_DATE_INDEX).compareTo(taskStartDate) == SAME_DAY_VALUE;
        }

        for (int day_index = STARTING_INDEX; day_index < LAST_DAY_INDEX; day_index++) {
            if (formattedDateList.get(day_index).compareTo(taskStartDate) == SAME_DAY_VALUE) {
                return TASK_FOUND;
            }

        }

        return TASK_NOT_FOUND;
    }
    /**
     * filter out event that start or end on that particular date
     * filter out ongoing event that is happening during that particular date
     */
    public boolean filterEvent(String taskStartDate, String taskEndDate) {
        if (findType.equals("DAY")) {
        	String formattedDate = formattedDateList.get(FORMATTED_DATE_INDEX);
        	return isEventFound(formattedDate, taskStartDate, taskEndDate);
            
        }

        for (int day_index = STARTING_INDEX; day_index < LAST_DAY_INDEX; day_index++) {
            String formattedDate = formattedDateList.get(day_index);
            if (isEventFound(formattedDate, taskStartDate, taskEndDate)) {
            	return TASK_FOUND;
            }

        }

        return TASK_NOT_FOUND;
    }
    
    private boolean isEventFound(String comparedDate, String startDate, String endDate) {
    	if (comparedDate.compareTo(startDate) == SAME_DAY_VALUE) {
    		return TASK_FOUND;
    	}
    	
       	if (comparedDate.compareTo(endDate) == SAME_DAY_VALUE) {
    		return TASK_FOUND;
    	}
       	
       	if (comparedDate.compareTo(startDate) >= AFTER_START_DATE && comparedDate.compareTo(endDate) <= BEFORE_END_DATE) {
       		return TASK_FOUND;
       	}
    	return TASK_NOT_FOUND;
    }

    private void getFormattedDate() {
        for (int date_index = STARTING_INDEX; date_index < dateToCompareList.size(); date_index++) {
            formattedDateList.add(dateToCompareList.get(date_index).format(format_exclude_time));
        }

    }

    public void getDateForCompare() {
        LocalDateTime dateToday = LocalDateTime.now();
        LocalDateTime dateForCompare = dateToday;
        Long timeToAdd = parseTimeToLong(nameKeyWords);

        if (findType.equals("DAY")) {
            dateForCompare = dateToday.plusDays(timeToAdd);
            dateToCompareList.add(dateForCompare);
            return;
        }

        LocalDateTime dateOfThatWeek = dateToday.plusWeeks(timeToAdd);
        int dayOfThatWeek = dateOfThatWeek.getDayOfWeek().getValue();
        LocalDateTime previousWeek = dateOfThatWeek.minusDays(dayOfThatWeek);
        LocalDateTime startOfTheWeek = previousWeek.plusDays(GET_TO_MONDAY_INDEX);

        for (int day_index = STARTING_INDEX; day_index < LAST_DAY_INDEX; day_index++) {
            dateForCompare = startOfTheWeek.plusDays(day_index);
            dateToCompareList.add(dateForCompare);
        }

    }

    public Long parseTimeToLong(Set<String> nameKeyWords) {
        List<String> getTimeList = new ArrayList<String>(nameKeyWords);
        return Long.parseLong(getTimeList.get(DATE_ARGS_INDEX));
    }

    @Override
    public String toString() {
        return "name=" + String.join(", ", nameKeyWords);
    }
}
