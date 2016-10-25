package seedu.address.commons.core;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javafx.util.Pair;

/**
 * @@author A0135768R
 * Config values used by the app
 */
public class Config {

    public static final String DEFAULT_CONFIG_FILE = "config.json";
    
    @JsonIgnore
    private static final Map<String, Pair<Class<?>, Object>> DEFAULT_CONFIGS = Maps.newHashMap() ;

    // Config values customizable through config file
    private Map<String, Object> configRegistry = Maps.newHashMap() ;
    
    static {

       Config.<String>registerNewConfigWithDefault("appTitle", "Task Force") ;
       Config.<String>registerNewConfigWithDefault("userPrefsFilePath", "preferences.json") ;
       Config.<String>registerNewConfigWithDefault("taskForceDataFilePath", "data/taskForceData.xml") ;
       Config.<String>registerNewConfigWithDefault("appName", "My Todo list") ;
       Config.<String>registerNewConfigWithDefault("activeHoursFrom", "0800") ;
       Config.<String>registerNewConfigWithDefault("activeHoursTo", "2100") ;
       Config.<Boolean>registerNewConfigWithDefault("enableSudo", false) ;
       
    }
    
    private static <T> void registerNewConfigWithDefault (String key, T value) {
        DEFAULT_CONFIGS.put(key, new Pair<Class<?>, Object>(value.getClass(), value)) ;
    }

    
    public Config () {
        registerDefaultConfigs() ;
    }
    
    private void registerDefaultConfigs() {
        for (Entry<String, Pair<Class<?>, Object>> entry : DEFAULT_CONFIGS.entrySet()) {
            
            Class<?> clazz = entry.getValue().getKey() ;
            Object value = entry.getValue().getValue() ;
            
            configRegistry.put(entry.getKey(), clazz.cast(value)) ;
            
        }
    }
    
    public <T> void setConfigurationOption (String key, T value) {
        
        configRegistry.replace(key, value) ;
    }
    
    public <T> T getConfigurationOption (String key) {
        if (!configRegistry.containsKey(key)) {
            return (T) DEFAULT_CONFIGS.get(key).getValue() ;
        }
        
        return (T) configRegistry.get(key) ;
    }
    
    public ImmutableMap<String, Pair<Class<?>, Object>> getDefaultConfigs() {
        return ImmutableMap.copyOf(DEFAULT_CONFIGS) ;
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
