package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ConfigCommand;

// @@author A0135768R
public class ConfigCommandTest extends TaskForceGuiTest {

    @Test
    public void configCommand_invalidCommandOptions_invalidConfigMessage() {

        // EP: empty values
        commandBox.runCommand("config");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ConfigCommand.MESSAGE_USAGE));

        // EP: invalid options
        commandBox.runCommand("config trolololol v/true");
        assertResultMessage(String.format(ConfigCommand.INVALID_CONFIG));

    }

    @Test
    public void configCommand_validCommandOptions_success() {
        commandBox.runCommand("config enableSudo v/true");
        assertResultMessage(String.format(ConfigCommand.MESSAGE_SUCCESS, "enableSudo", "true"));

        commandBox.runCommand("config appName v/New TaskForce");
        assertResultMessage(String.format(ConfigCommand.MESSAGE_SUCCESS, "appName", "New TaskForce"));
    }

}
