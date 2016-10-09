package seedu.address.logic.parser;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import seedu.address.commons.exceptions.IncorrectCommandException;

/**
 * Given a command string with arguments, this class will extract the values from the string
 * for easy retrieval
 * 
 * @author Jeremy Goh
 *
 */
public class ArgumentsParser {
	
	public static final ImmutableMap<String, CommandArgs> FLAGS ;
	
	static {
		
		Builder<String, CommandArgs> builder = new ImmutableMap.Builder<String, CommandArgs>() ;
		
		for (CommandArgs args : CommandArgs.values()) {
			builder.put(args.toString(), args) ;
		}
		
		FLAGS = builder.build() ;
	}
	
	private CommandArgs noFlagArgument ;
	private Set<CommandArgs> requiredArguments ;
	private Set<CommandArgs> optionalArguments ;
	private Multimap<CommandArgs, String> argumentValuesMap ;
	
	public ArgumentsParser() {
		requiredArguments = Sets.newHashSet() ;
		optionalArguments = Sets.newHashSet() ;
		argumentValuesMap = ArrayListMultimap.create();
		
		noFlagArgument = null ;
	}
	
	/**
	 * Adds a no flag argument to this parser. A no flag argument is defined to be an argument that appears at the very
	 * start of the command string. No flag arguments are required and there can only be one no flag argument in any
	 * command string
	 * 
	 * @param arg
	 * @return
	 */
	public ArgumentsParser addNoFlagArg (CommandArgs arg) {
		noFlagArgument = arg ;
		
		return this ;
	}
	
	/** 
	 * Adds a required argument. <p>
	 * 
	 * A required argument is defined to be an argument that if not present in the given command string,
	 * the command string will be deemed improper.
	 * 
	 * @param arg
	 * @return this object for daisy chaining.
	 */
	public ArgumentsParser addRequiredArg (CommandArgs arg) {
		requiredArguments.add(arg) ;
		
		return this ;
	}
	
	/** 
	 * Adds a optional argument. <p>
	 * 
	 * A optional argument is defined to be an argument that may or may not be present in the given command string.
	 * The command string will be deemed valid in both cases
	 * 
	 * @param arg
	 * @return this object for daisy chaining.
	 */
	public ArgumentsParser addOptionalArg (CommandArgs arg) {
		optionalArguments.add(arg) ;
		
		return this ;
	}
	
	/**
	 * Retrieves the associated values as a List of strings. <p>
	 * For example e/tag1 e/tag2 would return {tag1, tag2}. If there is only one value, this will return a singleton list.
	 * <p>
	 * If there is no such flag present, return an {@code Optional.empty()}.
	 * 
	 * @param arg
	 * @return
	 */
	public Optional<List<String>> getArgValue(CommandArgs arg) {
		List<String> result = Lists.newLinkedList() ;
		
		if (!argumentValuesMap.containsKey(arg)) {
			return Optional.empty() ;
		}
		
		result.addAll(argumentValuesMap.get(arg)) ;
		
		return Optional.of(result) ;
	}
	
	/**
	 * Returns true if and only if the parsed command string contains a flag-value pair
	 * containing at least one value
	 *  
	 * @param flag
	 * @return
	 */
	public boolean containsFlag(CommandArgs flag) {
		return argumentValuesMap.containsKey(flag) ;
	}
	
	/**
	 * Parses the given commandString into their individual flag-value pairs.
	 * 
	 * @param commandString
	 * @throws IncorrectCommandException if given command string does not match the required format
	 */
	public void parse(String commandString) throws IncorrectCommandException {
		
		Deque<Character> charStack = Lists.newLinkedList();
		CommandArgs thisArg = noFlagArgument ;
		
		commandString = commandString.trim().concat(" $/") ; // Append a unique "end of string" character
		
		for (int i = 0; i < commandString.length(); i ++) {
			
			if (commandString.charAt(i) != '/') {
				charStack.push(commandString.charAt(i));
			} else {
				CommandArgs nextArg = FLAGS.get(extractFlagFromString(charStack)) ;
				
				if (thisArg == null) {
					throw new IncorrectCommandException() ;
				} else {
					String value = extractArgValueFromString(charStack) ;
					
					if (value.length() == 0) {
						throw new IncorrectCommandException() ;
					}
					
					argumentValuesMap.put(thisArg, value) ;
					thisArg = nextArg ;
				}
			}
		}

		if (!isCommandStringValid()) {
			throw new IncorrectCommandException() ; 
		}
	}
	
	private String extractFlagFromString(Deque<Character> stack) {
		
		StringBuilder sb = new StringBuilder() ;
		sb.append('/') ;
		
		while (stack.peekFirst() != null && stack.peekFirst() != ' ') {
			sb.append(stack.pop()) ;
		}
		
		if (!stack.isEmpty()) {
			stack.pop() ;
		}
		
		return sb.reverse().toString() ;
	}
	
	private String extractArgValueFromString(Deque<Character> stack) {
		StringBuilder sb = new StringBuilder() ;
		
		while (stack.peekFirst() != null) {
			sb.append(stack.pop()) ;
		}
		
		return sb.reverse().toString() ;
	}
	
	/**
	 * A command string provided by the user is valid if and only if all required arguments are present.
	 * Requires the command string to be properly parsed first
	 * 
	 * @return true if command string provided by user is valid; false otherwise
	 */
	private boolean isCommandStringValid() {
		
		Set<CommandArgs> argsPresent = argumentValuesMap.keySet() ;
		
		if (argsPresent.containsAll(requiredArguments)) {		
			return true ;
		} else {
			return false ;
		}
	}
 }
