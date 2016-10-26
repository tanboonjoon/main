package seedu.address.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

// @@author A0135768R
public final class DateUtil {
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
	public static final LocalDateTime END_OF_TODAY = parseStringIntoDateTime("today 2359").get() ;
	public static final LocalDateTime NOW = LocalDateTime.now() ;
	public static final LocalDateTime MARKER_FOR_DELETE = parseStringIntoDateTime("01-01-2000 0000").get();
	public static final String STRING_FOR_DELETE = "-"; 
	
	public static Optional<LocalDateTime> parseStringIntoDateTime (String rawString) {
	    
	    if (rawString == null) {
	        return Optional.empty();
	    }
	    
	    if (rawString.equals(STRING_FOR_DELETE)) {	
	    	return Optional.of(MARKER_FOR_DELETE);
	    }
	    
		Parser dateParser = new Parser() ;
		
		
		List<DateGroup> dates = dateParser.parse(rawString) ;
		
		try {
	        Date date = dates.get(0).getDates().get(0) ;
	        
	        return Optional.of(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())) ;
	        
		} catch (IndexOutOfBoundsException e) {
		    return Optional.empty() ;
		}
	}
	
	public static String parseLocalDateTimeIntoString (LocalDateTime datetime) {
	    assert datetime != null ;
	    
		return datetime.format(FORMATTER) ;
	}
}
