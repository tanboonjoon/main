package seedu.address.logic.commands;



import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.storage.StorageManager;


/*
 * 
 * This command save the storage file into a different location
 * specified by the user
 */
//@@author A0139942W
public class CdCommand extends Command {

	public final static String COMMAND_WORD = "cd";
	public final static String MESSAGE_USAGE = COMMAND_WORD + ": change the filestorage saving location. \n"
			+ "Parameters : FILEPATH (must be a valid path) \n" 
			+ "Example :" + COMMAND_WORD + "C:\\Users\\Boon\\Desktop\\saveData.xml";
	public final static String MESSAGE_SUCCESS = "file has been saved to the location successfully in ";
	public final static String MESSAGE_FAILURE_FILE_TYPE = "please end the the filename with .xml ";
	public final static String MESSAGE_FAILURE_FILE_PATH = "please enter valid file path";
	
	private final String INVALID_FILE_PATH = null;
	private final String CONFIG_JSON_PATH = "config.json";
	private final String newStoragePath;
	private final String originalJsonPath;
	private Config config;
	
	private StorageManager storageManager;


	public CdCommand(String filepath) throws IllegalValueException {
		if (!checkFileType(filepath)) {
			throw new IllegalValueException(MESSAGE_FAILURE_FILE_TYPE);
		}
		
		if (!isValidPath(filepath)) {
			throw new IllegalValueException(MESSAGE_FAILURE_FILE_PATH);
		}
		

		config = new Config();
		originalJsonPath = config.getUserPrefsFilePath();
		this.newStoragePath = filepath;

		storageManager = new StorageManager(this.newStoragePath, originalJsonPath );
	}



	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		try {
			storageManager.saveTaskForce(model.getTaskForce());

			
			config.setTaskForceFilePath(this.newStoragePath);
			ConfigUtil.saveConfig(config, CONFIG_JSON_PATH);
	
			return new CommandResult(MESSAGE_SUCCESS + this.newStoragePath, true);

		} catch (IOException e) {
			return new CommandResult(MESSAGE_FAILURE_FILE_PATH);
		}

	}

	private boolean isValidPath(String filepath) {
		File file = new File(filepath);
		if (file.getParent() == INVALID_FILE_PATH) {
			return false;
		}
		File fileDir = new File(file.getParent());
		
		return fileDir.exists();
	}

	public boolean checkFileType(String filepath) {
		try {
			Path path = Paths.get(filepath);
			
			return path.toString().toLowerCase().endsWith(".xml");
		}catch (InvalidPathException e) {
			return false;
		}
		

	}
}
