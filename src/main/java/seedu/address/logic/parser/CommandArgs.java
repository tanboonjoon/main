package seedu.address.logic.parser;

public enum CommandArgs {
	NAME(""),
	DESC("d/"),
	TAGS("e/"),
	START_DATETIME ("st/"),
	END_DATETIME ("et/");
	
	private String commandString ;

	private CommandArgs (String cmd) {
		commandString = cmd ;
	}
	
	@Override
	public String toString() {
		return commandString ;
	}
}
