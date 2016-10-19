package seedu.address.logic.commands;

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
	
	public final String searchedDay;
	
	public FreetimeCommand(String searchedDay) {
		this.searchedDay = searchedDay;
	}

	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
