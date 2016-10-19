package guitests;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import seedu.address.commons.core.Config;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.logic.commands.CdCommand;


public class CdCommandTest extends TaskForceGuiTest {
	
	

	@Test
	public void invalid_fileType() {
		StringBuilder sb = new StringBuilder();
		String INVALID_FILE_TYPE_MESSAGE = sb.append(CdCommand.MESSAGE_FAILURE_FILE_TYPE)
        		.append("\n")
        		.append(CdCommand.MESSAGE_USAGE)
        		.toString();
        commandBox.runCommand("cd C:\\Users\\Boon\\Desktop\\asd.doc");
        assertResultMessage(INVALID_FILE_TYPE_MESSAGE);
        	
        commandBox.runCommand("cd C:\\Users\\Boon\\Desktop\\noType");
        assertResultMessage(INVALID_FILE_TYPE_MESSAGE);
	}
	
	@Test
	public void invalid_filePath() {
		StringBuilder sb = new StringBuilder();
		String INVALID_FILE_PATH_MESSAGE = sb.append(CdCommand.MESSAGE_FAILURE_FILE_PATH)
        		.append("\n")
        		.append(CdCommand.MESSAGE_USAGE)
        		.toString();
		
		commandBox.runCommand("cd C:\\Userads\\BoFDSon\\DessadkDFFDtop\\noType.xml");
		
        assertResultMessage(INVALID_FILE_PATH_MESSAGE);
        commandBox.runCommand("cd asd.xml");
        assertResultMessage(INVALID_FILE_PATH_MESSAGE);
        
        
	}
	
	@Test 
	public void valid_filePath() throws IOException {
		StringBuilder sb = new StringBuilder();
		String newPath = System.getProperty("user.dir");
		sb.append(newPath).append("\\src\\test\\java\\guitests\\forTesting.xml");
		System.out.println(sb.toString());
        commandBox.runCommand(("cd " + sb.toString()));
        assertResultMessage( (CdCommand.MESSAGE_SUCCESS + sb.toString()) );
        
        File file = new File("forTesting.xml");

        file.delete() ;
        
        File forDemoUse = new File("forDemoUse.xml");
        String forDemoUsePath = forDemoUse.getAbsolutePath();
        setUpOriginalPath(forDemoUsePath);
 
	}
	
	@After
	public void clear() {
		commandBox.runCommand("clear");
	}
	
	public void setUpOriginalPath(String path) throws IOException {
		Config config = new Config();
		config.setTaskForceFilePath(path);
		ConfigUtil.saveConfig(config, "config.json");
		
	}
	
	

}
