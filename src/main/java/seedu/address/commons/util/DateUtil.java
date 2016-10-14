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
	
	public static Optional<LocalDateTime> parseStringIntoDateTime (String rawString) {
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
		return datetime.format(FORMATTER) ;
	}
	
}
