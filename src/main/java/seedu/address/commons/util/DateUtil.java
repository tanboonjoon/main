package seedu.address.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public final class DateUtil {
	
	public static LocalDateTime parseStringIntoDateTime (String rawString) {
		Parser dateParser = new Parser() ;
		
		List<DateGroup> dates = dateParser.parse(rawString) ;
		Date date = dates.get(0).getDates().get(0) ;
		
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) ;
	}
	
}
