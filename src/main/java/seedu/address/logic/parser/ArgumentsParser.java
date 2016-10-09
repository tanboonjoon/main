package seedu.address.logic.parser;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
/**
 * Given a command string with arguments, this class will extract the values from the string
 * for easy retrieval
 * 
 * @author Uproller
 *
 */
public class ArgumentsParser {
	
	private Set<CommandArgs> requiredArguments ;
	private Set<CommandArgs> optionalArguments ;
	private Map<CommandArgs, String> argumentValuesMap ;
	
	public ArgumentsParser() {
		requiredArguments = Sets.newHashSet() ;
		optionalArguments = Sets.newHashSet() ;
		argumentValuesMap = Maps.newEnumMap(CommandArgs.class) ;
	}
	
	public ArgumentsParser addRequiredArgument (CommandArgs arg) {
		requiredArguments.add(arg) ;
		
		return this ;
	}
	
	public ArgumentsParser addOptionalArgument (CommandArgs arg) {
		optionalArguments.add(arg) ;
		
		return this ;
	}
	
	public void parse(String args) {
		
		for (int i = 0; i < args.length(); i++) {
			
		}
	}
}
