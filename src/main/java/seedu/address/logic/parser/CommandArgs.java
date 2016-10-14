package seedu.address.logic.parser;

public enum CommandArgs {
	NAME(""),
	DESC("d/"),
	TAGS("t/"),
	START_DATETIME ("st/"),
	END_DATETIME ("et/"),


	//Arguments for find command to parse
	FIND_DAY ("day/"),
	FIND_WEEK ("week/"),
	FIND_EVENT ("event/"),
	FIND_DEADLINE ("deadline/"),
	FIND_TASK ("reminder/"),
	FIND_ALL ("all/");
<<<<<<< a3b7c3fee4cf41d4e70a04345b906eb9e90dc676


=======
	
>>>>>>> modify findParser
	
	
	private String commandString ;

	private CommandArgs (String cmd) {
		commandString = cmd ;
	}
	
	@Override
	public String toString() {
		return commandString ;
	}
}
