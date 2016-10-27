package guitests;

import guitests.guihandles.HelpWindowHandle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HelpWindowTest extends TaskForceGuiTest {

    @Test
    public void openHelpWindow() {

//        taskListPanel.clickOnListView();
//
//        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
//
//        assertHelpWindowOpen(mainMenu.openHelpWindowUsingMenu());
//
    	assertHelpWindowOpen(commandBox.runHelpCommand());
    	
    	assertHelpWindowClose(commandBox.runHelpCommand());

        

    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }
    
    /*
     * @@author A0140037W
     */
    private void assertHelpWindowClose(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.closeWindowWithESCKey());
    }
}
