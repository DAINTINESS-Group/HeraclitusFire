package client;

//import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
//import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeConverterTest {

	public static void main(String[] args) {

		long unixSeconds1 = 1154698901;
		long unixSeconds2 = 1156863525;
		String formattedDate = convertEpochToHumanString(unixSeconds1);
		System.out.println(formattedDate);
		
		//export a method with a swtich case flag on the units
		//or, instead, return an array with the diff in all different kids of units.
		//or instead return an instance of your own class with the appropriate get methods
		Duration duration = computeDurationBetweenEpochTimes(unixSeconds1, unixSeconds2);
		long diffMinutes = duration.toMinutes();
		long diffHours = duration.toHours();
		long diffDays = duration.toDays();
		System.out.println(diffMinutes + "\t" + diffHours+ "\t" + diffDays + "\t" + duration.toString());
		
	}

	/**
	 * Returns the difference of two epoch Unix timestamps as a Duration
	 * which can allow calling toHours(), to Days(), toNano(), ...
	 * @param unixSeconds1 a long for the first epoch time to be compared
	 * @param unixSeconds2 a long for the second epoch time to be compared
	 * @return a Duration with the difference
	 */
	private static Duration computeDurationBetweenEpochTimes(long unixSeconds1, long unixSeconds2) {
		LocalDateTime dt1 = convertUnixTimeToLocalDateTime(unixSeconds1);
		LocalDateTime dt2 = convertUnixTimeToLocalDateTime(unixSeconds2);
		Duration duration = Duration.between(dt1, dt2);
		return duration;
	}

	/**
	 * Returns a local date time (not local date!) for unix time
	 * @param longValue the long value with the epoch unix time
	 * @return
	 */
	private static LocalDateTime convertUnixTimeToLocalDateTime(long longValue) {
		
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.of("UTC"));
	}
	
//	/**
//	 * Converts unix time to java date and a human readable representation
//	 * https://stackoverflow.com/questions/17432735/convert-unix-time-stamp-to-date-in-java
//	 * 
//	 * @param unixSeconds a long value with the unix timestamp
//	 * @return a string in human-readable format for the input string
//	 */
//	private static String convertUnixTimeToHumanString(long unixSeconds) {
//		// convert seconds to milliseconds
//		Date date = new java.util.Date(unixSeconds*1000L); 
//		// the format of your date
//		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
//		// give a timezone reference for formatting (see comment at the bottom)
//		sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC")); 
//		String formattedDate = sdf.format(date);
//		return formattedDate;
//	}

	/**
	 * Converts unix time to java date and a human readable representation
	 * https://stackoverflow.com/questions/17432735/convert-unix-time-stamp-to-date-in-java
	 * 
	 * @param unixTime a long value with the unxi timestamp
	 * @return a string in human readable format for the input string
	 */
	private static String convertEpochToHumanString(long unixTime) {
		final DateTimeFormatter formatter = 
			    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String _TIMEZONE = "UTC";
		final String formattedDtm = Instant.ofEpochSecond(unixTime)
			        .atZone(ZoneId.of(_TIMEZONE))
			        .format(formatter);
		return (formattedDtm); 
	}
}
