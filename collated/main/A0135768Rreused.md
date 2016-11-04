# reused
###### \java\seedu\address\commons\core\Config.java
``` java

    public String getTaskForceName() {
        return this.<String>getConfigurationOption("appName");
    }

    public void setTaskForceName(String taskForceName) {
        this.<String>setConfigurationOption("appName", taskForceName);
    }

    public String getAppTitle() {
        return this.<String>getConfigurationOption("appTitle");
    }

    public void setAppTitle(String appTitle) {
        this.<String>setConfigurationOption("appTitle", appTitle);
    }

    public Level getLogLevel() {
        return Level.INFO;
    }

    public void setLogLevel(Level logLevel) {
        // NO-OP
    }

    public String getUserPrefsFilePath() {
        return this.<String>getConfigurationOption("userPrefsFilePath");
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.<String>setConfigurationOption("userPrefsFilePath", userPrefsFilePath);
    }

    public String getTaskForceFilePath() {
        return this.<String>getConfigurationOption("taskForceDataFilePath");
    }

    public void setTaskForceFilePath(String taskForceFilePath) {
        this.<String>setConfigurationOption("taskForceDataFilePath", taskForceFilePath);

    }

}
```
###### \java\seedu\address\commons\util\FxViewUtil.java
``` java
    public static void applyAnchorBoundaryParameters(Node node, double left, double right, double top, double bottom) {
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setTopAnchor(node, top);
    }
}
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    @FXML
    private void handleCommandInputChanged() {
        // Take a copy of the command text
        saveCommandText();

        /*
         * We assume the command is correct. If it is incorrect, the command box
         * will be changed accordingly in the event handling code {@link
         * #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        mostRecentResult = logic.execute(previousCommandText);
        resultDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);
    }

```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     *  Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Invalid command: " + previousCommandText));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandText);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
    private void setAccelerators() {
        // helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
    }

    public void fillInnerParts() {
        // browserPanel = BrowserPanel.load(browserPlaceholder);
        taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getInitialTodaysTaskList());
        eventListPanel = EventListPanel.load(primaryStage, getEventListPlaceholder(), logic.getInitialTodaysTaskList()) ;
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getTaskForceFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
        freeTimeLine = FreeTimeLine.load(primaryStage, getResultDisplayPlaceholder());
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }
    
    public AnchorPane getEventListPlaceholder() {
        return eventListPanelPlaceholder ;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), (int) primaryStage.getX(),
                (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }
    
    public EventListPanel getEventListPanel() {
        return this.eventListPanel ;
    }

    public ResultDisplay getResultDisplay() {
        return resultDisplay;
    }

    public void loadTaskPage(ReadOnlyTask task) {
        // browserPanel.loadTaskPage(task);
    }

    public void releaseResources() {
        // browserPanel.freeResources();
    }
}
```
