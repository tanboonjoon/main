package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.core.Config;
import seedu.address.commons.events.storage.TaskForceStorageChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.storage.StorageManager;
import org.json.simple.JSONObject;

/*
 * 
 * This command save the storage file into a different location
 * specified by the user
 */
public class CdCommand extends Command {

	public final static String COMMAND_WORD = "cd";
	public final static String MESSAGE_USAGE = COMMAND_WORD + ": change the filestorage saving location. \n"
			+ "Parameters : FILEPATH (must be a valid path) \n" + "Example : cd destkop/important_folder/saveData.xml";
	public final static String MESSAGE_SUCCESS = "file has been saved to the location successfully in ";
	public final static String MESSAGE_FAILURE = "please enter a valid filepath";
	
	private final String newStoragePath;
	private final String CONFIG_JSON_PATH = "config.json";
	private final String originalJsonPath;
	private Config config;

	private StorageManager storageManager;


	public CdCommand(String filepath) throws IllegalValueException {
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
			if (!checkFileLocation(this.newStoragePath)) {
				System.out.println(checkFileLocation(this.newStoragePath));
				return new CommandResult(MESSAGE_FAILURE);
			}
		
			config.setTaskForceFilePath(this.newStoragePath);
			saveToConfigJSON(config);
			model.raiseEvent(new TaskForceStorageChangedEvent(storageManager, config));

			
			
			return new CommandResult(MESSAGE_SUCCESS + this.newStoragePath);

		} catch (IOException e) {
			return new CommandResult(MESSAGE_FAILURE);
		}

	}

	private void saveToConfigJSON(Config config) {
		// TODO Auto-generated method stub
	
		File oldConfigFile = new File(CONFIG_JSON_PATH);
		String path = oldConfigFile.getAbsolutePath();
		JSONObject = new JSONObject(config.toString());
		
	}

	public boolean checkFileLocation(String filepath) {
		Path path = Paths.get(filepath);
		return Files.exists(path);

	}

}
