package seedu.address.logic.parser;

public enum CommandArgs {
	DESC("d/"),
	TAGS("e/") ;
	
	private String commandString ;
	
	private CommandArgs (String cmd) {
		commandString = cmd ;
	}
	
	@Override
	public String toString() {
		return commandString ;
	}
}
