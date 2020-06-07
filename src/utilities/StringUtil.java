package utilities;

import java.util.GregorianCalendar;
import java.util.List;

//StringUtil class #435897647984356789223564879345678923564792780369547903568479
public class StringUtil {
    public static String join(List<String> strings) {
        StringBuilder joined = new StringBuilder();
        for (String s:strings) {
            joined.append(s).append(' ');
        }

        return joined.substring(0, joined.length()-1);
    }

    /**
     * a generalized date parser which takes in a date as well as a formatting
     * string, which represents how the parser should retrieve date information.
     * only absolute formats (no changes in spacing/data location) accepted
     * currently.
     *
     * numbers:
     *      year: YYYY/YY
     *      month: MM
     *      day: DD
     *      hour: HH
     *      minute: MM
     *      second: SS
     *
     * coming soon:
     * words:
     *      year: year (variable length...?)
     *      month:
     *          m (variable length)
     *          full month ("August", "July", etc.)
     *          mo3: truncated months ("aug", "jun", etc.)
     *      day: d (variable length)
     *      hour: h (variable length)
     *      minute: m (variable length)
     *      second: s (variable length)
     *      formatting:
     *          military vs. AM/PM
     *
     * @param date String representing the date information
     * @param format String representing the formatting of the date information
     * @return a Calendar date pertaining to the provided date information
     */
    public static GregorianCalendar getDate(String date, String format) {
        //set a "midpoint" where inferred dates (YY format)
        //must be calculated rigorously
        //this date specifically was picked for the FEHeroes wiki's handling of
        //"forever" dates (2038-01-19 03:14:07)
        final int YEAR_MIDPOINT = 1988;
        // 20 -- 1920 or 2020?
        // 1988-1920 = 68
        // 2020-1988 = 32 <- winner

        //noinspection UnusedAssignment
        int year = YEAR_MIDPOINT,
            month = 0, //january
            day = 1,
            hour = 0,
            minute = 0,
            second = 0;


        //get year
        int yearStart = format.indexOf('Y');
        int yearEnd = format.lastIndexOf('Y');
        year = Integer.parseInt(date.substring(yearStart, yearEnd+1));
        int yearDX = yearEnd-yearStart;
        if (yearDX==1) {
            year += 1900;
            if (Math.abs(year+100-YEAR_MIDPOINT)<Math.abs(year-YEAR_MIDPOINT)) {
                year += 100;
            }
        }

        //get month
        int monthStart = format.indexOf('M');                           //magic
        month = Integer.parseInt(date.substring(monthStart, monthStart+2))-1;

        //get day
        int dayStart = format.indexOf('D');
        day = Integer.parseInt(date.substring(dayStart, dayStart+2));

        //get hour
        int hourStart = format.indexOf('H');
        hour = Integer.parseInt(date.substring(hourStart, hourStart+2));

        //get minute
        int minStart = format.indexOf('M');
        minute = Integer.parseInt(date.substring(minStart, minStart+2));

        //get second
        int secStart = format.indexOf('S');
        second = Integer.parseInt(date.substring(secStart, secStart+2));


        //noinspection MagicConstant
        return new GregorianCalendar(
                year, month, day,
                hour, minute, second);
    }
}
