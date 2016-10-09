package seedu.address.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public final class DateUtil {
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
	
	public static LocalDateTime parseStringIntoDateTime (String rawString) {
		Parser dateParser = new Parser() ;
		
		List<DateGroup> dates = dateParser.parse(rawString) ;
		Date date = dates.get(0).getDates().get(0) ;
		
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) ;
	}
	
	public static String parseLocalDateTimeIntoString (LocalDateTime datetime) {
		return datetime.format(FORMATTER) ;
	}
	
}
