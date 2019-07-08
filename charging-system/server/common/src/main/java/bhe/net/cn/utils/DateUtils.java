package bhe.net.cn.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Note that this tool class has a date format of yyyy-MM-dd HH:mm:ss and cannot
 * be changed.
 *
 * @author Administrator
 */
public enum DateUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {

    }

    public static String get() {
        String date = null;
        try {
            date = DF.format(new Date());
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        return date;
    }

    public static Date strToDate(String date) {
        try {
            return DF.parse(date);
        } catch (ParseException e) {
            LOGGER.error(e.getLocalizedMessage());
            return null;
        }
    }

    public static String format(Date date) {
        if (date != null) {
            return DF.format(date);
        } else {
            return null;
        }
    }

    /**
     * Adds or subtracts the specified amount of time to the given calendar
     * field, based on the calendar's rules. For example, to subtract 5 days
     * from the current time of the calendar, you can achieve it by calling:
     *
     * <p>
     * <code>add(Calendar.DAY_OF_MONTH, -5)</code>
     * </p>
     *
     * @param field
     *            the calendar field
     * @param amount
     *            the amount of date or time to be added to the field
     * @return
     */
    public static String add(int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.add(field, amount);
        return DF.format(c.getTime());
    }

}
