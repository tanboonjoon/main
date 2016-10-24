package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Collections;
import javafx.util.Pair;
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

		model.updateFilteredTaskList(searchSet, SEARCH_TYPE);
		List<ReadOnlyTask> filteredList = model.getFilteredTaskList();

		LocalDateTime onThatDay = getThatDay();
		getAllEvent(filteredList);
		sortEventList();
		
		String freeTime = getFreeTime(onThatDay);
		return new CommandResult(freeTime);
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

		LocalDateTime currStartTime = timeList.get(FIRST_EVENT_INDEX).getKey();;
		LocalDateTime currEndTime = timeList.get(FIRST_EVENT_INDEX).getValue();
		
		
		if (timeList.size() == ONE_EVENT_ON_THAT_DAY) {
		    return freetimeForOneEvent(currStartTime , currEndTime, onThatDay, sb);
	
		}
	
		return freetimeForMutipleEvents(currStartTime, currEndTime, onThatDay, sb);


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
		if (doesEventStartEndSameDay(startTimeDay, endTimeDay, same_day)) {
			sb.append(String.format(FIRST_EVENT_MESSAGE, currentStartTime.format(hourFormat)));
		}
		
		if	(!doesEventEndOnSameDay(endTimeDay, same_day)) {
			return sb.toString();
		}
		

		return getAllFreeSlot(currentEndTime, same_day, sb);

	}
	
	private String getAllFreeSlot(LocalDateTime currentEndTime, int same_day, StringBuilder sb) {
		int nextEndDay;
		int currEndDay;
		LocalDateTime nextStartTime;
		LocalDateTime nextEndTime;
		for (int time_index = 1 ;  time_index < timeList.size(); time_index++) {
			nextStartTime = timeList.get(time_index).getKey();
			nextEndTime = timeList.get(time_index).getValue();
			nextEndDay = nextEndTime.getDayOfMonth();

			if (currentEndTime.isAfter(nextStartTime) || currentEndTime.isEqual(nextStartTime)) {
				currentEndTime = getNextEndTime(currentEndTime, timeList.get(time_index).getValue());
				continue;
			}
		
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, currentEndTime.format(hourFormat), nextStartTime.format(hourFormat)));
			currentEndTime = nextEndTime;
			
			if (!doesEventEndOnSameDay(nextEndDay, same_day)) {
				return sb.toString();
			}


		}
		currEndDay = currentEndTime.getDayOfMonth();
		
		if (!doesEventEndOnSameDay(currEndDay, same_day)) {
			return sb.toString();
		}
		
		return sb.append(String.format(LAST_EVENT_MESSAGE, currentEndTime.format(hourFormat))).toString();
		
	}


	private String freetimeForOneEvent(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime onThatDay, StringBuilder sb) {
		int startTimeDay = startTime.getDayOfMonth();
		int endTimeDay = endTime.getDayOfMonth();
		int sameDay = onThatDay.getDayOfMonth();
		sb.append(String.format(DEFAULT_STARTING_MESSAGE, onThatDay.format(dateFormat)));
		
		if (checkOnGoingEvent(startTimeDay, endTimeDay, sameDay)) {
			sb.append(String.format(ONGOING_EVENT_MESSAGE, startTime.format(datetimeFormat), endTime.format(datetimeFormat)));
			return sb.toString();
		}
		
		if (startTimeDay != sameDay && endTimeDay == sameDay) {
			sb.append(String.format(LAST_EVENT_MESSAGE, endTime.format(hourFormat)));
			return sb.toString();
		}
		
		sb.append(String.format(FIRST_EVENT_MESSAGE, startTime.format(hourFormat)));
		
		if (!doesEventEndOnSameDay(endTimeDay, sameDay)) {
			return sb.toString();
		}
		
		return sb.append(String.format(LAST_EVENT_MESSAGE, endTime.format(hourFormat))).toString();

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
		if (minutes < HALF_AN_HOUR) {
			return dateTime.plusMinutes(HALF_AN_HOUR - minutes);
		}

		return dateTime.plusMinutes(AN_HOUR - minutes);
	
	}
	
	private boolean checkOnGoingEvent(int startDay, int endDay, int checkDay) {
		return startDay != checkDay && endDay != checkDay;
	}
	
	private boolean doesEventStartEndSameDay(int startDay, int endDay, int checkDay) {
		return startDay == checkDay && endDay == checkDay;
	}
	
	private boolean doesEventEndOnSameDay(int endDay, int checkDay) {
		return endDay == checkDay;
	}
	
	private LocalDateTime getNextEndTime(LocalDateTime currEndTime, LocalDateTime nextEndTime) {
		if (nextEndTime.isAfter(currEndTime)) {
			return nextEndTime;
		}
		return currEndTime;
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

}
