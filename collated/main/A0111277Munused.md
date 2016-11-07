# A0111277Munused
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    private boolean getTaskByIndex(int targetIndex) {
        if (lastShownList.size() < targetIndex || targetIndex < 1) {
            indicateAttemptToExecuteIncorrectCommand();
            return false;
        }

        taskToEdit = lastShownList.get(targetIndex - 1);  
        return true;
    }
}
```
