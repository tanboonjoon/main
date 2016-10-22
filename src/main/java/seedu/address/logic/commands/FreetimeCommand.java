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



/*
 * Finds and list out all the timeslot that the user are free on that particular day
 * All timeslot are rounded up to block of 30min interval
 */
//@@author A0139942W
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
	
	private final String SEARCH_TYPE = "DAY";
;
	private final boolean DONE = true;
	private final int ZERO_EVENT_ON_THAT_DAY = 0;
	private final int ONE_EVENT_ON_THAT_DAY = 1;
	private final int SEARCHED_DAY_INDEX = 0;
	private final int FIRST_EVENT_INDEX = 0;
	private final int HALF_AN_HOUR = 30;
	private final int EXACT_AN_HOUR = 00;
	
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
		// TODO Auto-generated method stub
		model.updateFilteredTaskList(searchSet, SEARCH_TYPE);
		List<ReadOnlyTask> filteredList = model.getFilteredTaskList();

		LocalDateTime onThatDay = getThatDay();
		getAllEvent(filteredList);
		sortEventList();
		
		String freeTime = getFreeTime(onThatDay);
		return new CommandResult(freeTime);
	}
	

	private LocalDateTime getThatDay() {
		// TODO Auto-generated method stub
		List<String> getTimeArg = new ArrayList<String>(searchSet);
		Long timeToAdd = Long.parseLong(getTimeArg.get(SEARCHED_DAY_INDEX));
		LocalDateTime dateToday = LocalDateTime.now();
		return dateToday.plusDays(timeToAdd);
	}

	private String getFreeTime(LocalDateTime onThatDay) {
		// TODO Auto-generated method stub
		if (timeList.size() == ZERO_EVENT_ON_THAT_DAY) {
			return ZERO_EVENT_MESSAGE;
		}
		
		LocalDateTime currStartTime;
		LocalDateTime currEndTime;
		int same_day = onThatDay.getDayOfMonth();
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(DEFAULT_STARTING_MESSAGE, onThatDay.format(dateFormat)));
	
		if (timeList.size() == ONE_EVENT_ON_THAT_DAY) {
			currStartTime = timeList.get(FIRST_EVENT_INDEX).getKey();
		    currEndTime = timeList.get(FIRST_EVENT_INDEX).getValue();
		    return freetimeForOneEvent(currStartTime , currEndTime, same_day, sb);
	
		}
		
		currStartTime = timeList.get(FIRST_EVENT_INDEX).getKey();
		currEndTime = timeList.get(FIRST_EVENT_INDEX).getValue();
		
		return freetimeForMutipleEvents(currStartTime, currEndTime, onThatDay, sb);


	}
	
	private String freetimeForMutipleEvents(LocalDateTime startTime, LocalDateTime endTime,LocalDateTime thatDay, StringBuilder sb) {
		int same_day = thatDay.getDayOfMonth();
		// TODO Auto-generated method stub
		LocalDateTime currentStartTime = startTime;
		LocalDateTime currentEndTime = endTime;
		
		int startTimeDate = currentStartTime.getDayOfMonth();
		int endTimeDate = currentEndTime.getDayOfMonth();
		
		if (startTimeDate != same_day && endTimeDate != same_day) {
			sb.append(String.format(ONGOING_EVENT_MESSAGE, currentStartTime.format(datetimeFormat), currentEndTime.format(datetimeFormat)));
			return sb.toString();
		}
		if (startTimeDate == same_day) {
			sb.append(String.format(FIRST_EVENT_MESSAGE, currentStartTime.format(hourFormat)));
		}
		
		if	(endTimeDate != same_day) {
			return sb.toString();
		}

		return getAllFreeSlot(currentEndTime, same_day, sb);

	}
	
	private String getAllFreeSlot(LocalDateTime currentEndTime, int same_day,
			StringBuilder sb) {
		// TODO Auto-generated method stub
		for (int time_index = 1 ;  time_index < timeList.size(); time_index++) {
			LocalDateTime nextStartTime = timeList.get(time_index).getKey();
			LocalDateTime nextEndTime = timeList.get(time_index).getValue();
			

			if (currentEndTime.isAfter(nextStartTime) || currentEndTime.isEqual(nextStartTime)) {
				LocalDateTime tempEndTime = timeList.get(time_index).getValue();
				if (tempEndTime.isAfter(currentEndTime)) {
					currentEndTime = tempEndTime;
					continue;
				}
				continue;
			}
		
			sb.append(String.format(BETWEEN_EVENT_MESSAGE, currentEndTime.format(hourFormat), nextStartTime.format(hourFormat)));
			currentEndTime = nextEndTime;
			
			if (nextEndTime.getDayOfMonth() != same_day) {
				return sb.toString();
			}


		}
		return currentEndTime.getDayOfMonth() != same_day ? sb.toString() :
			sb.append(String.format(LAST_EVENT_MESSAGE, currentEndTime.format(hourFormat))).toString();
		
	}

	private String freetimeForOneEvent(LocalDateTime startTime, LocalDateTime endTime, int same_day, StringBuilder sb) {
		int startTimeDay = startTime.getDayOfMonth();
		int endTimeDay = endTime.getDayOfMonth();
		if (startTimeDay != same_day && endTimeDay != same_day) {
			sb.append(String.format(ONGOING_EVENT_MESSAGE, startTime.format(datetimeFormat), endTime.format(datetimeFormat)));
			return sb.toString();
		}
		if (startTimeDay != same_day) {
			sb.append(String.format(LAST_EVENT_MESSAGE, endTime.format(hourFormat)));
			return sb.toString();
		}
		
		sb.append(String.format(FIRST_EVENT_MESSAGE, startTime.format(hourFormat)));
		
		if (endTime.getDayOfMonth() != same_day) {
			return sb.toString();
		}
		
		return sb.append(String.format(LAST_EVENT_MESSAGE, endTime.format(hourFormat))).toString();

	}

	private void getAllEvent(List<ReadOnlyTask> filteredList) {
		// TODO Auto-generated method stub
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

		return dateTime.plusMinutes( Math.abs(HALF_AN_HOUR - minutes));
	
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
