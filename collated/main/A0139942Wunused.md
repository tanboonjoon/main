# A0139942Wunused
###### \java\seedu\address\logic\commands\CdCommand.java
``` java
	private String readConfig() throws FileNotFoundException, IOException, ParseException  {
		// TODO Auto-generated method stub

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(CONFIG_JSON_PATH));
		JSONObject configJson = (JSONObject) obj;
		
		String taskForceDataFilePath = (String) configJson.get("taskForceDataFilePath");
		return taskForceDataFilePath;
	}
}
```
