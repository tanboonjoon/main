package seedu.address.logic.commands;
/*
 * 
 * This command save the storage file into a different location
 * specified by the user
 */
public class CdCommand extends Command{
	
	public final static String COMMAND_WORD = "cd";
	public final static String MESSAGE_USAGE = COMMAND_WORD + ": change the filestorage saving location. \n"
			+ "Parameters : FILEPATH (must be a valid path) \n"
			+ "Example : cd /destkop/important_folder";
	

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
