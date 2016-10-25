package seedu.address.logic.commands;

import java.io.IOException;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ConfigUtil;

// @@author A0135768R
public class ConfigCommand extends Command {
    
    public static final String COMMAND_WORD = "config";
    
    public static final String INVALID_CONFIG = "The given config option is not valid!" ;
    public static final String INVALID_CONFIG_VALUE = "The given config option and or value is not valid!" ;
    
    public static final String MESSAGE_IOEXCEPTION = "Something went wrong when saving the config file! Your file might be corrupted."
            + "Please try to delete it and load the app up again for the default config file!" ;
    public static final String MESSAGE_SUCCESS = "The following configuration option: %1$s is successfully set to %2$s" ;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the given config option to the given value \n"
            + "Format: config CONFIG_OPTION v/NEW VALUE \n"
            + "Example: config activeHoursFrom v/1000" ;
    
    private static final String RESET_TO_DEFAULT = "default" ;
    
    private final String configOption ;
    private final String value ;
    private final Class<?> configValueType ;
    
    
    public ConfigCommand(String configOption, String value) throws IllegalValueException {
        this.configOption = configOption ;
        this.value = value ;
        this.configValueType = Config.getConfigValueType(configOption) ;
        
        if (configValueType == null) {
            throw new IllegalValueException(INVALID_CONFIG) ;
        }
    }
    
    @Override
    public CommandResult execute() {
        
        Config config = model.getConfigs() ;
        Object newConfigValue = value ;
        
        if (RESET_TO_DEFAULT.equalsIgnoreCase(value)) {
            final Object defaultValue = (config.getDefaultConfigs().get(configOption) != null) ? 
                    config.getDefaultConfigs().get(configOption).getValue() : null ;
             
            if (defaultValue == null) {
                return new CommandResult(INVALID_CONFIG) ;
            }
            
            newConfigValue = defaultValue.toString() ;
        }
        
        try {
            newConfigValue = castStringIntoValue(configValueType, newConfigValue) ;
            
            config.setConfigurationOption(configOption, newConfigValue );
            ConfigUtil.saveConfig(config, Config.DEFAULT_CONFIG_FILE);
        
        } catch (ClassCastException e) {
            return new CommandResult(INVALID_CONFIG_VALUE) ;
        
        } catch (IOException e) {
            return new CommandResult(MESSAGE_IOEXCEPTION) ;
        }
        
        
        return new CommandResult(String.format(MESSAGE_SUCCESS, configOption, newConfigValue), true) ;
    }
    
    private Object castStringIntoValue (Class<?> clazz, Object value) {
        
        if (String.class.getName().equals(clazz.getName())) {
            return value.toString() ;
        }
        
        if (Boolean.class.getName().equals(clazz.getName())) {
            return new Boolean(value.toString()) ;
        }
        
        if (Integer.class.getName().equals(clazz.getName())) {
            return Integer.parseInt(value.toString()) ;
        }
        
        return null ;
    }

}
