package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import edu.emory.mathcs.backport.java.util.Collections;
import javafx.util.Pair;
import seedu.address.commons.core.Config;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

//@@author A0139942W
/*
 * Finds and list out all the time slot that the user are free on that particular day 
 * All time slot are rounded up to block of 30min interval
 * It take into consideration user activeHour that are stored in config file. 
 */
public class FreetimeCommand extends Command {
    public static final String COMMAND_WORD = "freetime";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " : Find all timeslot that user are free on that particular day \n"
            + "freetime day/1 will give u all the free time you have tomorrow\n" + "Parameters: DAY/INTEGER \n"
            + "Example: " + COMMAND_WORD + " day/1\n" + COMMAND_WORD + " day/-2";

    public static final String INVALID_FREETIME_ARGS = "Please enter a valid number eg. freetime day/5";
    public static final String ZERO_EVENT_MESSAGE = "You are free for the day, trying clearing some reminders.";
    public static final String DEFAULT_STARTING_MESSAGE = "on %1$s: ";
    public static final String NO_OF_FREESLOT_MESSAGE = "you have %1$s freeslots";
    public static final String ONGOING_EVENT_MESSAGE = "There is a ongoing event from %1$s to %2$s \n";
    public static final String NO_FREE_TIME_MESSAGE = "There no freetime within the freetime period";

    private static final String SEARCH_TYPE = "DAY";

    private static final boolean DONE = true;
    private static final int ZERO_EVENT_ON_THAT_DAY = 0;
    private static final int ONE_EVENT_ON_THAT_DAY = 1;
    private static final int SEARCHED_DAY_INDEX = 0;
    private static final int FIRST_EVENT_INDEX = 0;
    private static final int HALF_AN_HOUR = 30;
    private static final int AN_HOUR = 60;
    private static final int EXACT_AN_HOUR = 00;
    private static final int LAST_TIME_BLOCK = 48;

    private ArrayList<Pair<LocalDateTime, LocalDateTime>> timeLists;
    private Map<Pair<Integer, Integer>, TimeStatus> freeTimes;
    private final Set<String> searchSets;

    private DateTimeFormatter dateFormat;
    private DateTimeFormatter datetimeFormat;
    private LocalDateTime activeHourStart;
    private LocalDateTime activeHourEnd;
    private StringBuilder freetimeMsgBuilder;
    private int noOfFreeSlot;
    
    public FreetimeCommand(String searchedDay) {
        this.searchSets = new HashSet<String>();
        this.searchSets.add(searchedDay);
        this.timeLists = new ArrayList<Pair<LocalDateTime, LocalDateTime>>();
        this.freeTimes = Maps.newHashMap();
        this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.datetimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        this.noOfFreeSlot = 0;
        this.freetimeMsgBuilder = new StringBuilder();
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(searchSets, SEARCH_TYPE, false);
        List<ReadOnlyTask> filteredList = model.getFilteredTaskList();

        LocalDateTime searchedDay = getSearchedDay();
        setActiveHours();
        getAllEvent(filteredList);
        sortEventList();

        String freeTime = getFreeTime(searchedDay);
        return new CommandResult(freeTime, true);
    }
    
    private void setActiveHours() {
        this.activeHourStart = roundUpTime(readActiveHours("activeHoursFrom"));
        this.activeHourEnd = roundUpTime(readActiveHours("activeHoursTo"));
    }


    private LocalDateTime readActiveHours(String key) {
        Config config = model.getConfigs();
        String activeTime = config.getConfigurationOption(key);
        return formatActiveHours(activeTime);
    }
    
    private LocalDateTime formatActiveHours(String activeHours) {
        LocalDateTime searchedDay = getSearchedDay();
        DateTimeFormatter withoutTime = DateTimeFormatter.ofPattern("dd/MM/yyyy ");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        String activeTimeDate = searchedDay.format(withoutTime) + activeHours;
        return LocalDateTime.parse(activeTimeDate, dateFormat);
    }

    private LocalDateTime getSearchedDay() {
        List<String> getTimeArg = new ArrayList<String>(searchSets);
        Long timeToAdd = Long.parseLong(getTimeArg.get(SEARCHED_DAY_INDEX));
        LocalDateTime dateToday = LocalDateTime.now();
        return dateToday.plusDays(timeToAdd);
    }

    private String getFreeTime(LocalDateTime onThatDay) {
        if (timeLists.size() == ZERO_EVENT_ON_THAT_DAY) {
            createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.FREE);
            return ZERO_EVENT_MESSAGE;
        }
        LocalDateTime currStartTime = timeLists.get(FIRST_EVENT_INDEX).getKey();
        LocalDateTime currEndTime = timeLists.get(FIRST_EVENT_INDEX).getValue();
        if (timeLists.size() == ONE_EVENT_ON_THAT_DAY) {
            return freetimeForOneEvent(currStartTime, currEndTime, onThatDay);
        }
        return freetimeForMutipleEvents(currStartTime, currEndTime, onThatDay);
    }

    
    private String freetimeForOneEvent(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime searchedDay) {
        LocalDateTime tempEndTime = endTime;
        freetimeMsgBuilder.append(String.format(DEFAULT_STARTING_MESSAGE, searchedDay.format(dateFormat)));     
        if (hasNoFreetime(startTime, endTime, searchedDay.getDayOfMonth())) {
            return freetimeMsgBuilder.toString();
        }
        if (activeHourStart.isBefore(startTime)) {
            createNewFreeTimeEntry(activeHourStart, startTime, TimeStatus.FREE);
        }
        if (activeHourStart.isAfter(endTime)) {
            tempEndTime = activeHourStart;
        }
        if (activeHourEnd.isAfter(endTime)) {
            createNewFreeTimeEntry(tempEndTime, activeHourEnd, TimeStatus.FREE);
        }
        return freetimeMsgBuilder.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
    }
    
    private boolean hasNoFreetime(LocalDateTime startTime, LocalDateTime endTime, int sameDay) {
        int startTimeDay = startTime.getDayOfMonth();
        int endTimeDay = endTime.getDayOfMonth();    
        if (checkOnGoingEvent(startTimeDay, endTimeDay, sameDay)) {
            freetimeMsgBuilder.append(String.format(ONGOING_EVENT_MESSAGE, startTime.format(datetimeFormat),
                    endTime.format(datetimeFormat)));
            createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
            return true;
        }    
        if (activeHourStart.isAfter(startTime) && activeHourEnd.isBefore(endTime)) {
            createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
            freetimeMsgBuilder.append(NO_FREE_TIME_MESSAGE).toString();
            return true;
        }     
        return false;
    }
    


    private String freetimeForMutipleEvents(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime searchedDay) {

        int same_day = searchedDay.getDayOfMonth();
        freetimeMsgBuilder.append(String.format(DEFAULT_STARTING_MESSAGE, searchedDay.format(dateFormat)));
        LocalDateTime currentStartTime = startTime;
        LocalDateTime currentEndTime = endTime;
        
        int startTimeDay = currentStartTime.getDayOfMonth();
        int endTimeDay = currentEndTime.getDayOfMonth();
        
        if (checkOnGoingEvent(startTimeDay, endTimeDay, same_day)) {
            createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
            freetimeMsgBuilder.append(String.format(ONGOING_EVENT_MESSAGE, currentStartTime.format(datetimeFormat),
                    currentEndTime.format(datetimeFormat)));
            return freetimeMsgBuilder.toString();
        }
        if (isTimeBeforeActiveHour(activeHourStart, currentStartTime)) {
            createNewFreeTimeEntry(activeHourStart, currentStartTime, TimeStatus.FREE);
        }
        if (isTimeBeforeActiveHour(activeHourEnd, currentEndTime)) {
            return freetimeMsgBuilder.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
        }
        if (isTimeAfterActiveHour(activeHourStart, currentEndTime)) {
            currentEndTime = activeHourStart;
        }
        return getAllFreeSlot(currentEndTime, same_day);

    }

    private String getAllFreeSlot(LocalDateTime currentEndTime, int sameDay) {
        boolean checkFreeTime = false;
        LocalDateTime tempCurrEndTime = currentEndTime;
        LocalDateTime nextStartTime;
        LocalDateTime nextEndTime;
        for (int timeIndex = 1; timeIndex < timeLists.size(); timeIndex++) {
            nextStartTime = timeLists.get(timeIndex).getKey();
            nextEndTime = timeLists.get(timeIndex).getValue();
            if (tempCurrEndTime.isAfter(nextStartTime) || tempCurrEndTime.isEqual(nextStartTime)) {
                tempCurrEndTime = getNextEndTime(tempCurrEndTime, timeLists.get(timeIndex).getValue());
                continue;
            }
            createNewFreeTimeEntry(tempCurrEndTime, nextStartTime, TimeStatus.FREE);
            if (isTimeBeforeActiveHour(activeHourEnd, nextEndTime)) {
                return freetimeMsgBuilder.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
            }
            tempCurrEndTime = nextEndTime;
            if (hasFreetime() == checkFreeTime) {
                continue;
            }
            checkFreeTime = true;

        }
        if (isTimeBeforeActiveHour(activeHourEnd, tempCurrEndTime) || activeHourEnd.isEqual(tempCurrEndTime)) {
            return hasFreetime() == checkFreeTime ? freetimeMsgBuilder.toString() : noFreeTimeForMutipleEvent(freetimeMsgBuilder);
        }
        createNewFreeTimeEntry(tempCurrEndTime, activeHourEnd, TimeStatus.FREE);
        return freetimeMsgBuilder.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
    }

    public String noFreeTimeForMutipleEvent(StringBuilder sb) {
        createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
        return sb.append(NO_FREE_TIME_MESSAGE).toString();
    }

    public void increaseNoOfFreeSlot() {
        noOfFreeSlot++;
    }

    private void getAllEvent(List<ReadOnlyTask> filteredList) {
        for (int listIndex = 0; listIndex < filteredList.size(); listIndex++) {
            if (!(filteredList.get(listIndex) instanceof Event)) {
                continue;
            }
            Event event = (Event) filteredList.get(listIndex);
            if (event.getDoneStatus() == DONE) {
                continue;
            }
            LocalDateTime startDate = roundUpTime(event.getStartDate());
            LocalDateTime endDate = roundUpTime(event.getEndDate());
            createNewFreeTimeEntry(startDate, endDate, TimeStatus.NOT_FREE);
            Pair<LocalDateTime, LocalDateTime> datePair = new Pair<LocalDateTime, LocalDateTime>(startDate, endDate);
            timeLists.add(datePair);
        }
    }

    private LocalDateTime roundUpTime(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        if (minutes == EXACT_AN_HOUR) {
            return dateTime;
        }
        if (minutes <= HALF_AN_HOUR) {
            return dateTime.plusMinutes(HALF_AN_HOUR - minutes);
        }
        return dateTime.plusMinutes(AN_HOUR - minutes);

    }

    private boolean checkOnGoingEvent(int startDay, int endDay, int checkDay) {
        return startDay != checkDay && endDay != checkDay;
    }

    private LocalDateTime getNextEndTime(LocalDateTime currEndTime, LocalDateTime nextEndTime) {
        if (nextEndTime.isAfter(currEndTime)) {
            return nextEndTime;
        }
        return currEndTime;
    }

    private boolean isTimeBeforeActiveHour(LocalDateTime activeTime, LocalDateTime time) {
        return activeTime.isBefore(time);
    }

    public boolean isTimeAfterActiveHour(LocalDateTime activeTime, LocalDateTime time) {
        return activeTime.isAfter(time);
    }

    public boolean hasFreetime() {
        return true;
    }

    private int convertTimeIntoInt(LocalDateTime dateToConvert) {
        int hour = dateToConvert.getHour();
        int totalMinutes = (hour * AN_HOUR) + dateToConvert.getMinute();
        return totalMinutes / HALF_AN_HOUR;
    }

    private void createNewFreeTimeEntry(LocalDateTime startDate, LocalDateTime endDate, TimeStatus status) {
        int startDay = startDate.getDayOfMonth();
        int endDay = endDate.getDayOfMonth();

        int start = convertTimeIntoInt(startDate);
        int end = convertTimeIntoInt(endDate);

        if (end == 0 && startDay != endDay) {
            end = LAST_TIME_BLOCK;
        }
        freeTimes.put(new Pair<Integer, Integer>(start, end), status);
        if (status.equals(TimeStatus.FREE)) {
            increaseNoOfFreeSlot();
        }
    }
    

    // sorting the list by start time
    private void sortEventList() {
        Collections.sort(timeLists, new Comparator<Pair<LocalDateTime, LocalDateTime>>() {
            public int compare(Pair<LocalDateTime, LocalDateTime> dateTimeOne,
                    Pair<LocalDateTime, LocalDateTime> dateTimeTwo) {
                return dateTimeOne.getKey().compareTo(dateTimeTwo.getKey());
            }

        });
    }

    /**
     * Returns a Map of timestatus representing the status of each timeslot.
     * Each pair represents a time slot, with the integer 0 representing 0000
     * hrs to 48 representing 2359 hrs. Each integer is 30 minutes.
     * <p>
     * 
     * 0 -> 1 : 0000 hrs to 0030 hrs.
     * <p>
     * Slots not in the map are assumed to be outside active hours
     * 
     */
    public Map<Pair<Integer, Integer>, TimeStatus> getFreeTimeLine() {
        return freeTimes;
    }

    public enum TimeStatus {
        FREE, NOT_FREE, OUTSIDE_ACTIVE_HRS;
    }
}
