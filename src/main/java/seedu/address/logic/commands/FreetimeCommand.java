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
 * Finds and list out all the timeslot that the user are free on that particular day 
 * All timeslot are rounded up to block of 30min interval
 */
public class FreetimeCommand extends Command{
	public static final String COMMAND_WORD = "freetime";
	
	public static final String MESSAGE_USAGE = COMMAND_WORD + " : Find all timeslot that user are free on that particular day \n"
			+ "freetime day/1 will give u all the free time you have tomorrow\n" 
			+ "Parameters: DAY/INTEGER \n"
			+ "Example: " + COMMAND_WORD + " day/1\n"
	        + COMMAND_WORD + " day/-2";
	
	public static final String INVALID_FREETIME_ARGS = "Please enter a valid number eg. freetime day/5";
	public static final String ZERO_EVENT_MESSAGE = "You are free for the day, trying clearing some reminders.";
	public static final String DEFAULT_STARTING_MESSAGE ="on %1$s: ";
	public static final String NO_OF_FREESLOT_MESSAGE ="you have %1$s freeslots";
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
	
	private ArrayList<Pair<LocalDateTime, LocalDateTime>> timeList;
	private Map<Pair<Integer, Integer>, TimeStatus> freeTimes ;
	private final Set<String> searchSet;

	
	private DateTimeFormatter dateFormat;
	private DateTimeFormatter datetimeFormat;
	private LocalDateTime activeHourStart;
	private LocalDateTime activeHourEnd;
	private int noOfFreeSlot;
	
	public FreetimeCommand(String searchedDay) {
		this.searchSet = new HashSet<String>();
		this.searchSet.add(searchedDay);
		timeList = new  ArrayList<Pair<LocalDateTime, LocalDateTime>> ();
		freeTimes = Maps.newHashMap() ;
		dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		datetimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
		noOfFreeSlot = 0;

	}



	@Override
	public CommandResult execute() {
		activeHourStart = roundUpTime(getActiveHour("activeHoursFrom"));
		activeHourEnd = roundUpTime(getActiveHour("activeHoursTo"));
		model.updateFilteredTaskList(searchSet, SEARCH_TYPE, false);
		List<ReadOnlyTask> filteredList = model.getFilteredTaskList();

		LocalDateTime onThatDay = getThatDay();
		getAllEvent(filteredList);
		sortEventList();
		
		String freeTime = getFreeTime(onThatDay);
		return new CommandResult(freeTime, true);
	}
	
	private LocalDateTime getActiveHour(String key) {
		// TODO Auto-generated method stub
		LocalDateTime onThatDay = getThatDay();
		Config config = model.getConfigs();
		String activeTime = config.getConfigurationOption(key);

		DateTimeFormatter withoutTime = DateTimeFormatter.ofPattern("dd/MM/yyyy ");
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
		String activeTimeDate = onThatDay.format(withoutTime) + activeTime;
		return LocalDateTime.parse(activeTimeDate, dateFormat);
	}
	

	private LocalDateTime getThatDay() {

		List<String> getTimeArg = new ArrayList<String>(searchSet);
		Long timeToAdd = Long.parseLong(getTimeArg.get(SEARCHED_DAY_INDEX));
		LocalDateTime dateToday = LocalDateTime.now();
		return dateToday.plusDays(timeToAdd);
	}

	private String getFreeTime(LocalDateTime onThatDay) {
		StringBuilder sb = new StringBuilder();
	
		if (timeList.size() == ZERO_EVENT_ON_THAT_DAY) {
		    createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.FREE) ;
			return ZERO_EVENT_MESSAGE;
		}

		LocalDateTime currStartTime = timeList.get(FIRST_EVENT_INDEX).getKey();
		LocalDateTime currEndTime = timeList.get(FIRST_EVENT_INDEX).getValue();
		
		
		if (timeList.size() == ONE_EVENT_ON_THAT_DAY) {
		    return freetimeForOneEvent(currStartTime , currEndTime, onThatDay, sb);
	
		}
	
		return freetimeForMutipleEvents(currStartTime, currEndTime, onThatDay, sb);


	}
	
	private String freetimeForOneEvent(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime onThatDay, StringBuilder sb) {
		int startTimeDay = startTime.getDayOfMonth();
		int endTimeDay = endTime.getDayOfMonth();
		int sameDay = onThatDay.getDayOfMonth();
		LocalDateTime tempEndTime = endTime;
		sb.append(String.format(DEFAULT_STARTING_MESSAGE, onThatDay.format(dateFormat)));
		
		if (checkOnGoingEvent(startTimeDay, endTimeDay, sameDay)) {
			sb.append(String.format(ONGOING_EVENT_MESSAGE, startTime.format(datetimeFormat), endTime.format(datetimeFormat)));
			createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE) ;
			return sb.toString();
		}
		
		if (isTimeAfterActiveHour(activeHourStart, startTime) && isTimeBeforeActiveHour(activeHourEnd, endTime)) {
		    createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE) ;
			return sb.append(NO_FREE_TIME_MESSAGE).toString();
		}
		
		if (isTimeBeforeActiveHour(activeHourStart, startTime)) {
		    createNewFreeTimeEntry(activeHourStart, startTime, TimeStatus.FREE) ;
		}
		
		if (isTimeAfterActiveHour(activeHourStart, endTime)) {
			tempEndTime = activeHourStart;
		}

		if (isTimeAfterActiveHour(activeHourEnd, endTime)) {
		    createNewFreeTimeEntry(tempEndTime, activeHourEnd, TimeStatus.FREE) ;
		}

		return sb.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
		

	}
	

	
	private String freetimeForMutipleEvents(LocalDateTime startTime, LocalDateTime endTime,LocalDateTime thatDay, StringBuilder sb) {
		
		int same_day = thatDay.getDayOfMonth();
		sb.append(String.format(DEFAULT_STARTING_MESSAGE, thatDay.format(dateFormat)));
		LocalDateTime currentStartTime = startTime;
		LocalDateTime currentEndTime = endTime;
		
		int startTimeDay = currentStartTime.getDayOfMonth();
		int endTimeDay = currentEndTime.getDayOfMonth();
		
		if (checkOnGoingEvent(startTimeDay, endTimeDay, same_day)) {
		    createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
			sb.append(String.format(ONGOING_EVENT_MESSAGE, currentStartTime.format(datetimeFormat), currentEndTime.format(datetimeFormat)));
			return sb.toString();
		}
		
		if (isTimeBeforeActiveHour(activeHourStart, currentStartTime)) {
		    createNewFreeTimeEntry(activeHourStart, currentStartTime, TimeStatus.FREE) ;
		}
		
		
		if (isTimeBeforeActiveHour(activeHourEnd, currentEndTime)) {
			return sb.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
		}
		
		if (isTimeAfterActiveHour(activeHourStart, currentEndTime)) {
			currentEndTime = activeHourStart;
		}

		
		return getAllFreeSlot(currentEndTime, same_day, sb);

	}
	
	private String getAllFreeSlot(LocalDateTime currentEndTime, int same_day, StringBuilder sb) {
		boolean checkFreeTime = false;
		LocalDateTime tempCurrEndTime = currentEndTime;
		LocalDateTime nextStartTime;
		LocalDateTime nextEndTime;
		for (int time_index = 1 ;  time_index < timeList.size(); time_index++) {
			nextStartTime = timeList.get(time_index).getKey();
			nextEndTime = timeList.get(time_index).getValue();

			if (tempCurrEndTime.isAfter(nextStartTime) || tempCurrEndTime.isEqual(nextStartTime)) {
				tempCurrEndTime = getNextEndTime(tempCurrEndTime, timeList.get(time_index).getValue());
				continue;
			}
			
			createNewFreeTimeEntry(tempCurrEndTime, nextStartTime, TimeStatus.FREE) ;			
			
			if (isTimeBeforeActiveHour(activeHourEnd , nextEndTime)) {
				return sb.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
			}
			

			
			tempCurrEndTime = nextEndTime;

			if (hasFreetime() == checkFreeTime) {
				continue;
			}
			checkFreeTime = true;

		}
		if (isTimeBeforeActiveHour(activeHourEnd , tempCurrEndTime) || activeHourEnd.isEqual(tempCurrEndTime)) {
			return hasFreetime() == checkFreeTime ? sb.toString() 
					: noFreeTimeForMutipleEvent(sb);
		}
		
		createNewFreeTimeEntry(tempCurrEndTime, activeHourEnd, TimeStatus.FREE);
		return sb.append(String.format(NO_OF_FREESLOT_MESSAGE, noOfFreeSlot)).toString();
		
	}

	public String noFreeTimeForMutipleEvent(StringBuilder sb) {
		createNewFreeTimeEntry(activeHourStart, activeHourEnd, TimeStatus.NOT_FREE);
		return sb.append(NO_FREE_TIME_MESSAGE).toString();
	}
	
	public void increaseNoOfFreeSlot() {
		noOfFreeSlot++;
	}

	private void getAllEvent(List<ReadOnlyTask> filteredList) {

		for(int list_index = 0 ; list_index < filteredList.size(); list_index++ ) {
			
			if(!(filteredList.get(list_index) instanceof Event)) {
				continue;
			}
			
			Event event = (Event) filteredList.get(list_index);
			if (event.getDoneStatus() == DONE) {
				continue;
			}
			
			LocalDateTime startDate = roundUpTime(event.getStartDate());
			LocalDateTime endDate = roundUpTime(event.getEndDate());
			Pair<LocalDateTime, LocalDateTime> datePair = new Pair<LocalDateTime, LocalDateTime> (startDate, endDate);
			timeList.add(datePair);
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
	
	private int convertTimeIntoInt (LocalDateTime dateToConvert) {		
	    int hour = dateToConvert.getHour();
	    int totalMinutes = (hour * AN_HOUR) + dateToConvert.getMinute();
	    return totalMinutes / HALF_AN_HOUR;
	}
	
	private void createNewFreeTimeEntry(LocalDateTime startDate, LocalDateTime endDate, TimeStatus status) {
		int startDay = startDate.getDayOfMonth();
		int endDay = endDate.getDayOfMonth();
		
	    int start = convertTimeIntoInt(startDate) ;
	    int end = convertTimeIntoInt(endDate) ;
	    
	    if (end == 0 && startDay != endDay) {
	    	end = LAST_TIME_BLOCK;
	    }
	    
	    freeTimes.put(new Pair<Integer, Integer>(start, end), status) ;    
	    if (status.equals(TimeStatus.FREE)) {
	    	increaseNoOfFreeSlot();
	    }
	}
	
	//sorting the list by start time
	private void sortEventList() {
		Collections.sort(timeList, new Comparator<Pair<LocalDateTime , LocalDateTime>>() 
		{
			public int compare(Pair<LocalDateTime, LocalDateTime> dateTimeOne, Pair<LocalDateTime, LocalDateTime> dateTimeTwo) {
				return dateTimeOne.getKey().compareTo(dateTimeTwo.getKey());
			}

		});
	}
	
	/**
	 * Returns a Map of timestatus representing the status of each timeslot.
	 * Each pair represents a time slot, with the integer 0 representing 0000 hrs to 48 representing 2359 hrs.
	 * Each integer is 30 minutes.
	 * <p>
	 * 
	 * 0 -> 1 : 0000 hrs to 0030 hrs.
	 * <p>
	 * Slots not in the map are assumed to be outside active hours
	 * 
	 */
	public Map <Pair<Integer, Integer>, TimeStatus> getFreeTimeLine() {
	    return freeTimes ;
	}
	
	
	public enum TimeStatus {
	    
	    FREE,
	    NOT_FREE,
	    OUTSIDE_ACTIVE_HRS ;
	    
	}
}
