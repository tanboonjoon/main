package guitests;

import org.junit.Test;

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
        
	}
	
	@Test 
	public void valid_filePath() {
        commandBox.runCommand("cd C:\\Users\\Boon\\Desktop\\hey.xml");
        assertResultMessage( (CdCommand.MESSAGE_SUCCESS + "C:\\Users\\Boon\\Desktop\\hey.xml") );
	}
	
	

}
