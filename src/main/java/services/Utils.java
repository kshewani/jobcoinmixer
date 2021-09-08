package services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    final static String DATE_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSSz";

    /**
     * Converts date in string format to LocalDateTime.
     * @param dateStr
     * @return
     */
    public static LocalDateTime stringToDate(String dateStr) {
        return LocalDateTime.parse(dateStr,
                DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));
    }

    /**
     * Converts date to string format.
     * @param dateTime
     * @return
     */
    public static String dateToString(ZonedDateTime dateTime) {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER).format(dateTime);
    }
}
