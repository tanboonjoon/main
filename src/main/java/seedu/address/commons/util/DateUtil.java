package seedu.address.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import javafx.util.Pair;
import seedu.address.logic.filters.NameQualifier;
import seedu.address.logic.filters.PredicateExpression;
import seedu.address.model.Model;
import seedu.address.model.task.Event;
import seedu.address.model.task.ReadOnlyTask;

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

    /**
     * Given a new event that is being added/to be added to the model, checks that if any events currently in the model
     * that will conflict with the given event. If the given event is already added to the model, it will be ignored - that is
     * an event cannot conflict with itself.
     * 
     * @param model     The model of the TaskForce - cannot be null.
     * @param eventToBeAdded    The event that is being added/or already added to the model.
     * @return      {@link Optional.empty()} if there is no conflict; otherwise an arbitrary conflicting event is returned.
     */
    public static Optional<Event> checkForConflictingEvents (Model model, Event eventToBeAdded) {
        assert model != null ;

        LocalDateTime startTime = eventToBeAdded.getStartDate() ;
        LocalDateTime endTime = eventToBeAdded.getEndDate() ;

        assert startTime.isBefore(endTime) ;

        Long days = ChronoUnit.DAYS.between(startTime, endTime) ;

        model.searchTaskList(new PredicateExpression(new NameQualifier(Sets.newHashSet(days.toString()), NameQualifier.FILTER_BY_DAY)) );

        PriorityQueue<Event> pq = new PriorityQueue<>(new EventComparator()) ;

        for (ReadOnlyTask task : model.getSearchedTaskList()) {

            if ( !(task instanceof Event) || task.equals(eventToBeAdded) ) {
                continue ;
            }

            pq.add((Event) task) ;

        }

        // Don't need to do anything if there is no events occuring on this time period
        if (pq.isEmpty()) {
            return Optional.empty() ;
        }

        Event event = pq.poll() ;

        while (event.getEndDate().isAfter(startTime)) {

            if (event.getStartDate().isBefore(endTime)) {
                return Optional.of(event);
            }

            event = pq.poll() ;
        }

        return Optional.empty() ;

    }


    private static Optional<Pair<LocalDateTime, LocalDateTime>> determineStartAndEndDateTime(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate, boolean canBeEmpty) {

        LocalDateTime computedStartDate ;
        LocalDateTime computedEndDate ;

        if (!startDate.isPresent() && !endDate.isPresent()) {
            return Optional.empty() ;
        }
        
        if (!canBeEmpty && (!startDate.isPresent() || !endDate.isPresent())) {
            return Optional.empty() ;
        }

        if (!startDate.isPresent() || !endDate.isPresent()) {
            computedStartDate = startDate.orElse(DateUtil.NOW) ;
            computedEndDate = endDate.orElse(startDate.get().withHour(23).withMinute(59)) ;

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;
        }

        if (endDate.get().isBefore(startDate.get()) && isDateComponentSameAsNow(endDate.get())) {
            computedStartDate = startDate.get() ;

            int seconds = endDate.get().getSecond() ;
            int minutes = endDate.get().getMinute() ;
            int hours = endDate.get().getHour() ;

            computedEndDate = startDate.get().withHour(hours).withMinute(minutes).withSecond(seconds) ;

            if (computedStartDate.isBefore(computedEndDate)) {
                return Optional.of(new Pair<LocalDateTime, LocalDateTime> (computedStartDate, computedEndDate)) ;
            }

        } else if (endDate.get().isAfter(startDate.get())) {

            return Optional.of(new Pair<LocalDateTime, LocalDateTime> (startDate.get(), endDate.get())) ;

        }

        return Optional.empty() ;

    }


    /**
     * Given one and start and end date, the function will return a corresponding start and end dates following these rules: <p>
     * 
     * - If both dates are empty, it will return Optional.empty <br>
     * - If only start date is empty, the start date shall assumed to be now.
     * <br>
     * - If only end date is empty, the end date shall be assumed to be on the same day of the start date at 2359
     * <br>
     * - If a start datetime is provided and only end time is provided, the end datetime is assumed to be on the same day as the start
     * date on the time provided.
     * <p>
     * If start datetime happens to be after end datetime, this function will return Optional.empty()
     * <p>
     * 
     * If the boolean allowEmptyValues is set to false, however, then if any dates is empty, this will return {@link Optional.empty()}
     * This is set to true by default if not specified
     * 
     * <p>
     * 
     * @param startString string that will be parsed by natty
     * @param endString string that will be parsed by natty
     * @return a LocalDateTime pair with the key being the starting datetime, and the value the ending datetime
     */
    public static Optional<Pair<LocalDateTime, LocalDateTime>> determineStartAndEndDateTime(String startString, String endString) {
        return determineStartAndEndDateTime(parseStringIntoDateTime(startString), parseStringIntoDateTime(endString), true) ;
    }
    
    public static Optional<Pair<LocalDateTime, LocalDateTime>> determineStartAndEndDateTime(String startString, String endString, boolean allowEmptyValues) {
        return determineStartAndEndDateTime(parseStringIntoDateTime(startString), parseStringIntoDateTime(endString), allowEmptyValues) ;
    }

    private static boolean isDateComponentSameAsNow (LocalDateTime dateTime) {
        LocalDate now = LocalDate.now() ;
        LocalDate givenDate = dateTime.toLocalDate() ;

        return now.equals(givenDate) ;
    }
    
    public static String getRelativeDateFromNow (LocalDateTime dateTime) {
        Long milis = ChronoUnit.MILLIS.between(NOW, dateTime) ;
        
        return RelativeTimeConverter.toDuration(milis) ;
    }
    
    public static long getTimeDifferenceFromNow (LocalDateTime dateTime, ChronoUnit units) {
        long diff = units.between(NOW, dateTime) ;
        
        return diff ;
    }

    // Comparator for priority queue
    private static class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event arg0, Event arg1) {
            LocalDateTime arg0Date = arg0.getEndDate() ;
            LocalDateTime arg1Date = arg1.getEndDate() ;

            if (arg0Date.isBefore(arg1Date)) {
                return 1 ;
            }

            if (arg0Date.isAfter(arg1Date)) {
                return -1 ;
            }

            return 0 ;
        }

    }

    private static class RelativeTimeConverter {
        
        public static final ImmutableList<Long> TIMES = ImmutableList.of(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1) );
        
        public static final ImmutableList<String> TIME_STIRNGS = ImmutableList.of("year","month","day","hour","minute","second");

        
        public static String toDuration(long miliseconds) {
            
            long duration = Math.abs(miliseconds) ;

            StringBuffer sb = new StringBuffer();
            
            for(int i = 0; i < TIMES.size(); i ++) {
                Long current = TIMES.get(i);
                long temp = duration/current;
                
                if(temp>0) {
                    sb.append(temp) ;
                    sb.append(" ") ;
                    sb.append( TIME_STIRNGS.get(i) ) ;
                    sb.append(temp > 1 ? "s" : "") ;
                    
                    break;
                }
            }
            
            if( "".equals(sb.toString()) ) {
                return "0 second ago";
            } 
            
            if (miliseconds > 0) {
                sb.append(" later") ;
            
            } else {
                sb.append(" ago") ;
            }
            
            return sb.toString();
        }
    }
}
