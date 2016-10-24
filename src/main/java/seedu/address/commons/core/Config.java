package seedu.address.commons.core;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.common.collect.Maps;



/**
 * @@author A0135768R
 * Config values used by the app
 */
public class Config {

    public static final String DEFAULT_CONFIG_FILE = "config.json";

    // Config values customizable through config file
    private Map<String, Object> configRegistry = Maps.newHashMap() ;
    
    public Config () {
        registerDefaultConfigs() ;
    }
    
    private void registerDefaultConfigs() {
        this.<String>registerNewConfigOption("appTitle", "Task Force") ;
        this.<String>registerNewConfigOption("userPrefsFilePath", "preferences.json") ;
        this.<String>registerNewConfigOption("taskForceDataFilePath", "data/taskForceData.xml") ;
        this.<String>registerNewConfigOption("appName", "My Todo list") ;
        this.<String>registerNewConfigOption("activeHoursFrom", "0800") ;
        this.<String>registerNewConfigOption("activeHoursTo", "2100") ;
        
    }
    
    private <T> void registerNewConfigOption (String key, T value) {
        configRegistry.put(key, value) ;
    }
    
    public <T> void setConfigurationOption (String key, T value) {
        
        configRegistry.replace(key, value) ;
    }
    
    public <T> T getConfigurationOption (String key) {
        if (!configRegistry.containsKey(key)) {
            return null ;
        }
        
        return (T) configRegistry.get(key) ;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof Config)){ //this handles null as well.
            return false;
        }

        Config o = (Config)other;

        return configRegistry.equals(o.configRegistry) ;
    }

    @Override
    public int hashCode() {
        return configRegistry.hashCode() ;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for (Entry<String, Object> entry : configRegistry.entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue()) ;
            sb.append("\n") ;
        }
        
        return sb.toString();
    }
    
    public static String getDefaultConfigString() {
        Config config = new Config () ;
        
        return config.toString() ;
    }
    
    // The getter and setters are left alone for minimal code impact
    // @@author reused
    
    public String getTaskForceName() {
        return this.<String>getConfigurationOption("appName") ;
    }

    public void setTaskForceName(String taskForceName) {
        this.<String>setConfigurationOption("appName", taskForceName) ;
    }
    
    public String getAppTitle() {
        return this.<String>getConfigurationOption("appTitle") ;
    }

    public void setAppTitle(String appTitle) {
        this.<String>setConfigurationOption("appTitle", appTitle) ;
    }

    public Level getLogLevel() {
        return Level.INFO ;
    }

    public void setLogLevel(Level logLevel) {
        // NO-OP
    }

    public String getUserPrefsFilePath() {
        return this.<String>getConfigurationOption("userPrefsFilePath") ;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.<String>setConfigurationOption("userPrefsFilePath", userPrefsFilePath) ;
    }

    public String getTaskForceFilePath() {
        return this.<String>getConfigurationOption("taskForceDataFilePath") ;
    }

    public void setTaskForceFilePath(String taskForceFilePath) {
        this.<String>setConfigurationOption("taskForceDataFilePath", taskForceFilePath) ;
        
    }

}
