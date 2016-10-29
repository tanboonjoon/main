package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public static final String DEFAULT_STARTING_MESSAGE ="for %1$s you are free on: \n";
	public static final String FIRST_EVENT_MESSAGE = "before %1$s \n";
	public static final String LAST_EVENT_MESSAGE = "after %1$s \n";
	public static final String BETWEEN_EVENT_MESSAGE = "%1$s to %2$s \n";
	public static final String ONGOING_EVENT_MESSAGE = "You are not free because you have a ongoing from %1$s to %2$s \n";
	public static final String NO_FREE_TIME_MESSAGE = "Sorry you do not have any freetime between your active period";
	
	private static final String SEARCH_TYPE = "DAY";

	private static final boolean DONE = true;
	private static final int ZERO_EVENT_ON_THAT_DAY = 0;
	private static final int ONE_EVENT_ON_THAT_DAY = 1;
	private static final int SEARCHED_DAY_INDEX = 0;
	private static final int FIRST_EVENT_INDEX = 0;
	private static final int HALF_AN_HOUR = 30;
	private static final int AN_HOUR = 60;
	private static final int EXACT_AN_HOUR = 00;
	
	private ArrayList<Pair<LocalDateTime, LocalDateTime>> timeList;
	private final Set<String> searchSet;

	
	private DateTimeFormatter dateFormat;
	private DateTimeFormatter hourFormat;
	private DateTimeFormatter datetimeFormat;
	private LocalDateTime activeHourStart;
	private LocalDateTime activeHourEnd;
	
	public FreetimeCommand(String searchedDay) {
		this.searchSet = new HashSet<String>();
		this.searchSet.add(searchedDay);
		timeList = new  ArrayList<Pair<LocalDateTime, LocalDateTime>> ();
		dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		hourFormat = DateTimeFormatter.ofPattern("HHmm");
		datetimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

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
			return sb.toString();
		}
		
		if (isTimeAfterActiveHour(activeHourStart, startTime) && isTimeBeforeActiveHour(activeHourEnd, endTime)) {
			return sb.append(NO_FREE_TIME_MESSAGE).toString();
		}
		
		if (isTimeBeforeActiveHour(activeHourStart, startTime)) {
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, activeHourStart.format(hourFormat), startTime.format(hourFormat)));
		}
		
		if (isTimeAfterActiveHour(activeHourStart, endTime)) {
			tempEndTime = activeHourStart;
		}

		if (isTimeAfterActiveHour(activeHourEnd, endTime)) {
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, tempEndTime.format(hourFormat), activeHourEnd.format(hourFormat)));
			return sb.toString();
		}

		return sb.toString();
		

	}
	

	
	private String freetimeForMutipleEvents(LocalDateTime startTime, LocalDateTime endTime,LocalDateTime thatDay, StringBuilder sb) {
		
		int same_day = thatDay.getDayOfMonth();
		sb.append(String.format(DEFAULT_STARTING_MESSAGE, thatDay.format(dateFormat)));
		LocalDateTime currentStartTime = startTime;
		LocalDateTime currentEndTime = endTime;
		
		int startTimeDay = currentStartTime.getDayOfMonth();
		int endTimeDay = currentEndTime.getDayOfMonth();
		
		if (checkOnGoingEvent(startTimeDay, endTimeDay, same_day)) {
			sb.append(String.format(ONGOING_EVENT_MESSAGE, currentStartTime.format(datetimeFormat), currentEndTime.format(datetimeFormat)));
			return sb.toString();
		}
		
		if (isTimeBeforeActiveHour(activeHourStart, currentStartTime)) {
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, activeHourStart.format(hourFormat), currentStartTime.format(hourFormat)));
		}
		
		
		if (isTimeBeforeActiveHour(activeHourEnd, currentEndTime)) {
			return sb.toString();
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
			
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, tempCurrEndTime.format(hourFormat), nextStartTime.format(hourFormat)));
			
			if (isTimeBeforeActiveHour(activeHourEnd , nextEndTime)) {
				return sb.toString();
			}
			

			
			tempCurrEndTime = nextEndTime;

			if (hasFreetime() == checkFreeTime) {
				continue;
			}
			checkFreeTime = true;

		}
		if (isTimeBeforeActiveHour(activeHourEnd , tempCurrEndTime) || activeHourEnd.isEqual(tempCurrEndTime)) {
			return hasFreetime() == checkFreeTime ? sb.toString() 
					: sb.append(NO_FREE_TIME_MESSAGE).toString();
		}
		

		return sb.append(String.format(BETWEEN_EVENT_MESSAGE, tempCurrEndTime.format(hourFormat), activeHourEnd.format(hourFormat))).toString();
		
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
	 * Returns a map of timestatus representing the status of each timeslot.
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
	    return null ;
	}
	
	
	public enum TimeStatus {
	    
	    FREE,
	    NOT_FREE,
	    OUTSIDE_ACTIVE_HRS ;
	    
	}
}
