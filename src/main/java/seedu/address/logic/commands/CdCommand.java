package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.XmlTaskForceStorage;

/*
 * 
 * This command save the storage file into a different location
 * specified by the user
 */
public class CdCommand extends Command{
	
	public final static String COMMAND_WORD = "cd";
	public final static String MESSAGE_USAGE = COMMAND_WORD + ": change the filestorage saving location. \n"
			+ "Parameters : FILEPATH (must be a valid path) \n"
			+ "Example : cd destkop/important_folder/saveData.xml";
	public final static String MESSAGE_SUCCESS = "file has been saved to the location successfully.";
	public final static String MESSAGE_FAILURE = "please enter a valid filepath";
	
	private final String newStoragePath;
	private final String originalStoragePath;
	private Config config;
	private Storage storage;
	private XmlTaskForceStorage xmlStorageRetriever;
	
	public CdCommand(String filepath) throws IllegalValueException{
		config = new Config();
		originalStoragePath = config.getTaskForceFilePath();
		this.newStoragePath = filepath;
		xmlStorageRetriever = new XmlTaskForceStorage(newStoragePath);
	}



	@Override
	public CommandResult execute()  {
		// TODO Auto-generated method stub


		
		return new CommandResult(MESSAGE_SUCCESS);
	}
	
	public boolean isValidFilePath(String filepath) {
		File file = new File(filepath);
		System.out.println(file.exists());
		System.out.println(file.isDirectory());
		return file.isDirectory();
	}

}
