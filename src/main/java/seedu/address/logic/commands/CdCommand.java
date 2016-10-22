package seedu.address.logic.commands;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	public final static String MESSAGE_SUCCESS_CHANGE = "file has been saved to the location successfully in \n%1$s";
	public final static String MESSAGE_SUCCESS_CHECK = "Your current saveData is located in \n%1$s ";
	public final static String MESSAGE_FAILURE_FILE_TYPE = "please end the the filename with .xml ";
	public final static String MESSAGE_FAILURE_FILE_PATH = "please enter valid file path";
	public final static String MESSAGE_FAILURE_PARSE = "config.json cant be parsed/found, rerun the program to reinitiate the file";
	public final static String CD_CHANGE = "change";
	public final static String CD_CHECK = "check";
	
	private final boolean INVALID_FILE_ARGS = false;
	private final String INVALID_FILE_PATH = null;
	private final String CONFIG_JSON_PATH = "config.json";
	private final String newStoragePath;
	private final String originalJsonPath;
	private final String currentSavePath;
	private final String commandType;
	private Config config;
	
	private StorageManager storageManager;


	public CdCommand(String filepath, String commandType) throws IllegalValueException, ParseException, FileNotFoundException, IOException  {
		
		if (commandType.equals(CD_CHANGE)) {
			checkForInvalidArgs(filepath);
		}

	
		this.currentSavePath = readConfig();
		this.commandType = commandType;
		this.config = new Config();
		this.originalJsonPath = config.getUserPrefsFilePath();
		this.newStoragePath = filepath;
		this.storageManager = new StorageManager(this.newStoragePath, originalJsonPath );
	}



	private void checkForInvalidArgs(String filepath) throws IllegalValueException, ParseException {
		// TODO Auto-generated method stub
		if (!checkFileType(filepath)) {
			throw new IllegalValueException(MESSAGE_FAILURE_FILE_TYPE);
		}
		
		if (!isValidPath(filepath)) {
			throw new IllegalValueException(MESSAGE_FAILURE_FILE_PATH);
		}
	}







	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		if (this.commandType.equals(CD_CHECK)) {
			
			return new CommandResult(String.format(MESSAGE_SUCCESS_CHECK, this.currentSavePath));
		}
		
		try {
			storageManager.saveTaskForce(model.getTaskForce());

			
			config.setTaskForceFilePath(this.newStoragePath);
			ConfigUtil.saveConfig(config, CONFIG_JSON_PATH);
	
			return new CommandResult(String.format(MESSAGE_SUCCESS_CHANGE , this.newStoragePath));

		} catch (IOException e) {
			return new CommandResult(MESSAGE_FAILURE_FILE_PATH);
		}

	}
	private String readConfig() throws FileNotFoundException, IOException, ParseException  {
		// TODO Auto-generated method stub

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(CONFIG_JSON_PATH));
		JSONObject configJson = (JSONObject) obj;
		
		String taskForceDataFilePath = (String) configJson.get("taskForceDataFilePath");
		return taskForceDataFilePath;
	}




	private boolean isValidPath(String filepath) {
		File file = new File(filepath);
		if (file.getParent() == INVALID_FILE_PATH) {
			return INVALID_FILE_ARGS;
		}
		File fileDir = new File(file.getParent());
		
		return fileDir.exists();
	}

	public boolean checkFileType(String filepath) {
		try {
			Path path = Paths.get(filepath);
			
			return path.toString().toLowerCase().endsWith(".xml");
		}catch (InvalidPathException e) {
			return INVALID_FILE_ARGS;
		}
		

	}

}
