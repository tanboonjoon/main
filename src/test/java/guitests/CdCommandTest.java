package guitests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Config;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.logic.commands.CdCommand;

//@@A0139942W
public class CdCommandTest extends TaskForceGuiTest {
	
	private String invalidFileType;
	private String invalidMissingFileType;
	private String currentStoragePath;
	private String validPath;
	private String INVALID_FILE_TYPE_MESSAGE;
	private String INVALID_FILE_PATH_MESSAGE;
	@Before
	
	public void setUp() {
		Config config = new Config();
		
		currentStoragePath = config.getTaskForceFilePath();
		String userPath = System.getProperty("user.dir");
		invalidFileType = userPath.concat("\\asd.doc");
		invalidMissingFileType = userPath.concat("\\asd");
		
		validPath = userPath.concat("\\src\\test\\java\\guitests\\forTesting.xml");
	}
	
	@Before
	public void setUpInvalidMessagePath() {
		StringBuilder sb = new StringBuilder();
		INVALID_FILE_TYPE_MESSAGE = 
				sb.append(CdCommand.MESSAGE_FAILURE_FILE_TYPE)
				.append("\n")
				.append(CdCommand.MESSAGE_USAGE)
				.toString();
	}	
	
	@Before
	public void setUpInvalidMessageType() {
		StringBuilder sb = new StringBuilder();
		INVALID_FILE_PATH_MESSAGE =  
				sb.append(CdCommand.MESSAGE_FAILURE_FILE_PATH)
        		.append("\n")
        		.append(CdCommand.MESSAGE_USAGE)
        		.toString();
	}
	@Test
	public void invalid_fileType() {

        commandBox.runCommand("cd " + invalidFileType);
        assertResultMessage(INVALID_FILE_TYPE_MESSAGE);
        	
        commandBox.runCommand("cd " + invalidMissingFileType);
        assertResultMessage(INVALID_FILE_TYPE_MESSAGE);
	}
	
	@Test
	public void invalid_filePath() {

		
		commandBox.runCommand("cd C:\\INVALID\\DONT_EXIST\\PATH\\saveData.xml");
        assertResultMessage(INVALID_FILE_PATH_MESSAGE);
        commandBox.runCommand("cd asd.xml");
        assertResultMessage(INVALID_FILE_PATH_MESSAGE);
        
        
	}
	
	@Test
	public void valid_getSaveDataLocation() {
		commandBox.runCommand("cd");
		assertResultMessage(String.format(CdCommand.MESSAGE_SUCCESS_CHECK, currentStoragePath));
	}
	
	@Test 
	public void valid_filePath() throws IOException {

        commandBox.runCommand("cd " + validPath);
        assertResultMessage( String.format(CdCommand.MESSAGE_SUCCESS_CHANGE, validPath) );
        
       
        File file = new File("./src/test/java/guitests/forTesting.xml");
        assertTrue(file.exists());
        file.delete() ;

 
	}
	
	@After
	public void clear() {
		commandBox.runCommand("clear");
	}
	
	
	//change the savepath back to the original xml file after test is finished
	@AfterClass
	public static void setUpOriginalPath() throws IOException {
		File forDemoUse = new File("forDemoUse.xml");
		String forDemoUsePath = forDemoUse.getAbsolutePath();
		Config config = new Config();
		config.setTaskForceFilePath(forDemoUsePath);
		ConfigUtil.saveConfig(config, "config.json");
		
	}
	
	

}
