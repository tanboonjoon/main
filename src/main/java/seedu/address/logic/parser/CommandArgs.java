package seedu.address.logic.parser;

public enum CommandArgs {
	NAME(""),
	DESC("d/"),
	TAGS("t/"),
	START_DATETIME ("st/"),
	END_DATETIME ("et/"),
	//Arguments for find command to parse
	FIND_DAY ("d/"),
	FIND_WEEK ("w/"),
	FIND_EVENT ("e/"),
	FIND_DEADLINE ("dl/"),
	FIND_TASK ("r/"),
	FIND_ALL ("a/");
	
	
	
	private String commandString ;

	private CommandArgs (String cmd) {
		commandString = cmd ;
	}
	
	@Override
	public String toString() {
		return commandString ;
	}
}
