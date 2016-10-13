package seedu.address.logic.commands;


public interface Undoable {


    /**
     * 
     * 
     * @return true if the command deem undoable or else return false.
     */
    public boolean isUndoableCommand();

}
