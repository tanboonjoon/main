package seedu.address.logic.commands;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import seedu.address.model.TaskForce;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String CLEAR_OPERATION_CANCELED = "Clear operation canceled!";
    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "TaskForce data has been cleared!";
    public static final String SUDO_REQUIRED = "SUDO mode needs to be required to perform this action." ;

    

    @Override
    public CommandResult execute() {
        assert model != null;

        if (!model.getConfigs().<Boolean>getConfigurationOption("enableSudo")) {
            return new CommandResult(SUDO_REQUIRED) ;
        }
        if(getConfirmationDialog()){
            model.resetData(TaskForce.getEmptyTaskForce());
            return new CommandResult(MESSAGE_SUCCESS, true);
        }else{
            return new CommandResult(CLEAR_OPERATION_CANCELED, false);
        }

    }
    
    /*
     * @@author A0140037W
     * return true if user click Ok. Else return false.
     */
    private boolean getConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("This operation is not undoable. If proceed, all information will be deleted forever. Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
        
    }
}
