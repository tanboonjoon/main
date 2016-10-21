package seedu.address.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import javafx.util.Pair;

// @@author A0135768R
public final class DateUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    public static final LocalDateTime END_OF_TODAY = parseStringIntoDateTime("today 2359").get() ;
    public static final LocalDateTime NOW = LocalDateTime.now() ;

    public static Optional<LocalDateTime> parseStringIntoDateTime (String rawString) {

        if (rawString == null) {
            return Optional.empty() ;
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


    private static Optional<Pair<LocalDateTime, LocalDateTime>> determineStartAndEndDateTime(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {

        LocalDateTime computedStartDate ;
        LocalDateTime computedEndDate ;

        if (!startDate.isPresent() && !endDate.isPresent()) {
            computedStartDate = DateUtil.NOW ;
            computedEndDate = DateUtil.END_OF_TODAY ;

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;
        }

        if (!startDate.isPresent() && endDate.isPresent()) {
            computedStartDate = DateUtil.NOW ;
            computedEndDate = endDate.get() ;

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;
        }

        if (startDate.isPresent() && !endDate.isPresent()) {
            computedStartDate = startDate.get() ;
            computedEndDate = startDate.get().withHour(23).withMinute(59) ;

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;
        }



        if (endDate.get().isBefore(startDate.get()) && isDateComponentSameAsNow(endDate.get())) {
            computedStartDate = startDate.get() ;

            int seconds = endDate.get().getSecond() ;
            int minutes = endDate.get().getMinute() ;
            int hours = endDate.get().getHour() ;

            computedEndDate = startDate.get().withHour(hours).withMinute(minutes).withSecond(seconds) ;

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;

        }

        return Optional.empty() ;

    }


    /**
     * Given one and start and end date, the function will return a corresponding start and end dates following these rules: <p>
     * 
     * - If startdate and enddate are both empty, this function will return starting date of now and enddate of 2359 later today
     * <br>
     * - If only start date is empty, the start date shall assumed to be now.
     * <br>
     * - If only end date is empty, the end date shall be assumed to be on the same day of the start date at 2359
     * <br>
     * - If a start datetime is provided and only end time is provided, the end datetime is assumed to be on the same day as the start
     * date on the time provided.
     * <p>
     * If start datetime happens to be after end datetime, this function will return Optional.empty()
     * 
     * @param startString string that will be parsed by natty
     * @param endString string that will be parsed by natty
     * @return a LocalDateTime pair with the key being the starting datetime, and the value the ending datetime
     */
    public static Optional<Pair<LocalDateTime, LocalDateTime>> determineStartAndEndDateTime(String startString, String endString) {
        return determineStartAndEndDateTime(parseStringIntoDateTime(startString), parseStringIntoDateTime(endString)) ;
    }

    private static boolean isDateComponentSameAsNow (LocalDateTime dateTime) {
        LocalDate now = LocalDate.now() ;
        LocalDate givenDate = LocalDate.from(dateTime) ;

        return now.equals(givenDate) ;
    }
}
