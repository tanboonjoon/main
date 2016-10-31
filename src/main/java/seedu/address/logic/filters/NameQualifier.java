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

//@@author A0139942W
/**
 *
 * 
 * A general qualifier used by the filteredList wrapper to filter tasks
 * according to user's input
 *
 */
public class NameQualifier implements Qualifier {

    public static final String FILTER_BY_DAY = "DAY";
    public static final String SEARCH_NAME = "NAME";
    public static final String SEARCH_DESC = "DESC";
    public static final String SEARCH_TAG = "TAG";
    public static final String SEARCH_TYPE = "TYPE";
    public static final String SEARCH_TYPE_ALL = "all";
    public static final String SEARCH_TYPE_OVERDUE = "overdue";
    public static final String SEARCH_TYPE_MARK = "mark";

    private static final boolean TASK_NOT_FOUND = false;
    private static final boolean TASK_FOUND = true;
    private static final boolean MARKED_TASK = true;
    private static final boolean MARK_NOT_FILTERED = false;
    private static final int STARTING_INDEX = 0;
    private static final int LAST_DAY_INDEX = 7;

    private static final int SAME_DAY_VALUE = 0;
    private static final int AFTER_START_DATE = 0;
    private static final int BEFORE_END_DATE = 0;
    private static final int ARGS_INDEX = 0;
    private static final int FORMATTED_DATE_INDEX = 0;
    private static final int GET_TO_MONDAY_INDEX = 1;
    private static final int OVERDUE_TASK = -1;

    private Set<String> nameKeyWords;
    private String typeOfFind;
    private DateTimeFormatter format_exclude_time;
    private ArrayList<String> formattedDateList;
    private ArrayList<LocalDateTime> dateToCompareList;
    private boolean isMarkCheck;

    public NameQualifier(Set<String> nameKeyWords, String typeOfFind, boolean isMarkCheck) {

        this.formattedDateList = new ArrayList<String>();
        this.dateToCompareList = new ArrayList<LocalDateTime>();
        this.format_exclude_time = DateTimeFormatter.ofPattern("ddMMyyyy");
        this.nameKeyWords = nameKeyWords;
        this.typeOfFind = typeOfFind;
        this.isMarkCheck = isMarkCheck;
    }

    @Override
    public boolean run(ReadOnlyTask task) {

        if (isTypeSearch(typeOfFind)) {
            return filterByType(task);
        }

        if (isMarkCheck == MARK_NOT_FILTERED && task.getDoneStatus() == MARKED_TASK) {
            return TASK_NOT_FOUND;
        }
        if (isKeywordSearch(typeOfFind)) {
            Trie keywordTrie = buildKeyword();
            return filterByKeyWord(task, keywordTrie, typeOfFind);
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

    private boolean isTypeSearch(String searchType) {
        // TODO Auto-generated method stub
        return searchType.equals(SEARCH_TYPE);
    }

    private boolean isKeywordSearch(String searchType) {
        return searchType.equals(SEARCH_NAME) || searchType.equals(SEARCH_TAG) || searchType.equals(SEARCH_DESC);
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

    private boolean filterByType(ReadOnlyTask task) {
        // TODO Auto-generated method stub
        List<String> findTypeList = new ArrayList<String>(nameKeyWords);
        String findType = findTypeList.get(ARGS_INDEX);
        if (SEARCH_TYPE_ALL.equalsIgnoreCase(findType)) {
            return TASK_FOUND;
        }

        if (SEARCH_TYPE_MARK.equalsIgnoreCase(findType)) {
            return task.getDoneStatus() == MARKED_TASK;
        }

        return filterByOverdue(task);
    }

    private boolean filterByOverdue(ReadOnlyTask task) {
        // TODO Auto-generated method stub
        if (task.getDoneStatus() == MARKED_TASK) {
            return TASK_NOT_FOUND;
        }
        LocalDateTime now = LocalDateTime.now();
        if (task instanceof Deadline) {
            LocalDateTime endDate = ((Deadline) task).getEndDate();
            return endDate.compareTo(now) <= OVERDUE_TASK;
        }

        return TASK_NOT_FOUND;
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
        UniqueTagList tagList = task.getTags();

        for (Tag tag : tagList) {
            if (keywordTrie.containsMatch(tag.tagName)) {
                return TASK_FOUND;
            }
        }

        return TASK_NOT_FOUND;
    }

    public boolean filterDeadLine(String taskStartDate) {

        if ("DAY".equals(typeOfFind)) {
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
     * filter out event that start or end on that particular date filter out
     * ongoing event that is happening during that particular date
     */
    public boolean filterEvent(String taskStartDate, String taskEndDate) {

        if (FILTER_BY_DAY.equals(typeOfFind)) {
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

        if (comparedDate.compareTo(startDate) >= AFTER_START_DATE
                && comparedDate.compareTo(endDate) <= BEFORE_END_DATE) {
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

        if (FILTER_BY_DAY.equals(typeOfFind)) {
            dateForCompare = dateToday.plusDays(timeToAdd);
            dateToCompareList.add(dateForCompare);
            return;
        }

        LocalDateTime startOfTheWeek = getToDesiredWeek(timeToAdd, dateToday);

        for (int day_index = STARTING_INDEX; day_index < LAST_DAY_INDEX; day_index++) {
            dateForCompare = startOfTheWeek.plusDays(day_index);
            dateToCompareList.add(dateForCompare);
        }

    }

    public LocalDateTime getToDesiredWeek(Long addedTime, LocalDateTime now) {
        LocalDateTime dateOfThatWeek = now.plusWeeks(addedTime);
        int dayOfThatWeek = dateOfThatWeek.getDayOfWeek().getValue();
        LocalDateTime previousWeek = dateOfThatWeek.minusDays(dayOfThatWeek);
        return previousWeek.plusDays(GET_TO_MONDAY_INDEX);
    }

    public Long parseTimeToLong(Set<String> nameKeyWords) {
        List<String> getTimeList = new ArrayList<String>(nameKeyWords);
        return Long.parseLong(getTimeList.get(ARGS_INDEX));
    }

    @Override
    public String toString() {
        return "name=" + String.join(", ", nameKeyWords);
    }
}
