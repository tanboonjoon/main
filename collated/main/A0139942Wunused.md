# A0139942Wunused
###### \java\seedu\address\logic\commands\CdCommand.java
``` java
    /*
     * model now always store the updated version of config.json file
     * there no longer any need to read the config.json directly.
     */
    private String readConfig() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(CONFIG_JSON_PATH));
        JSONObject configJson = (JSONObject) obj;
        String taskForceDataFilePath = (String) configJson.get("taskForceDataFilePath");
        return taskForceDataFilePath;
    }
}
```
