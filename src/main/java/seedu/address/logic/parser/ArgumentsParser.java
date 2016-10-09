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
	
	public ArgumentsParser addNoFlagArg (CommandArgs arg) {
		noFlagArgument = arg ;
		
		return this ;
	}
	
	public ArgumentsParser addRequiredArg (CommandArgs arg) {
		requiredArguments.add(arg) ;
		
		return this ;
	}
	
	public ArgumentsParser addOptionalArg (CommandArgs arg) {
		optionalArguments.add(arg) ;
		
		return this ;
	}
	
	public Optional<List<String>> getArgValue(CommandArgs arg) {
		List<String> result = Lists.newLinkedList() ;
		
		if (!argumentValuesMap.containsKey(arg)) {
			return Optional.empty() ;
		}
		
		result.addAll(argumentValuesMap.get(arg)) ;
		
		return Optional.of(result) ;
	}
	
	public void parse(String args) throws IncorrectCommandException {
		
		Deque<Character> charStack = Lists.newLinkedList();
		CommandArgs thisArg = noFlagArgument ;
		
		args = args.trim() ;
		
		for (int i = 0; i < args.length(); i ++) {
			
			if (args.charAt(i) != '/') {
				charStack.push(args.charAt(i));
			} else {
				CommandArgs nextArg = FLAGS.get(extractFlagFromString(charStack)) ;
				
				if (thisArg == null) {
					throw new IncorrectCommandException() ;
				} else {
					argumentValuesMap.put(thisArg, extractArgValueFromString(charStack)) ;
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
		
		while (stack.peekFirst() != ' ') {
			sb.append(stack.pop()) ;
		}
		
		stack.pop() ;
		
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
