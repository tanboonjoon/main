package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private final String SEARCH_TYPE = "DAY";
	private final int HALF_AN_HOUR = 30;
	public final String searchedDay;
	private final Set<String> searchSet;
	private ArrayList<Pair<LocalDateTime, LocalDateTime>> timeList;
	
	public FreetimeCommand(String searchedDay) {
		this.searchedDay = searchedDay;
		this.searchSet = new HashSet<String>();
		this.searchSet.add(searchedDay);
		timeList = new  ArrayList<Pair<LocalDateTime, LocalDateTime>> ();
	}

	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		model.updateFilteredTaskList(searchSet, SEARCH_TYPE);
		List<ReadOnlyTask> filteredList = model.getFilteredTaskList();

		
		getAllEvent(filteredList);
		return new CommandResult("you are free!");
	}
	

	private void getAllEvent(List<ReadOnlyTask> filteredList) {
		// TODO Auto-generated method stub
		for(int list_index = 0 ; list_index < filteredList.size(); list_index++ ) {
			
			if(!(filteredList.get(list_index) instanceof Event)) {
				continue;
			}
			Event event = (Event) filteredList.get(list_index);
			LocalDateTime startDate = roundUpTime(event.getStartDate());
			LocalDateTime endDate = roundUpTime(event.getEndDate());
			Pair<LocalDateTime, LocalDateTime> datePair = new Pair<LocalDateTime, LocalDateTime> (startDate, endDate);
			timeList.add(datePair);
		}
	}
	private LocalDateTime roundUpTime(LocalDateTime dateTime) {
		int minutes = dateTime.getMinute();
		if (minutes > HALF_AN_HOUR) {
			LocalDateTime newDateTime = dateTime.plusMinutes(HALF_AN_HOUR - minutes);
			return newDateTime;
		}
		LocalDateTime newDateTime = dateTime.minusMinutes(minutes);
		return newDateTime;
	}

}
