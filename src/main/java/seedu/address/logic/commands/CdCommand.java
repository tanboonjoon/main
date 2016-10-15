package seedu.address.logic.commands;
/*
 * 
 * This command save the storage file into a different location
 * specified by the user
 */
public class CdCommand extends Command{
	
	public final String COMMAND_WORD = "cd";
	private String filepath;
	
	public CdCommand(String filepath) {
		this.filepath = filepath;
	}
	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
