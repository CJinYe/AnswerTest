package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-1-7 16:02
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DateTimeUtil {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String dateToString(Date data, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        //        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return format.format(data);
    }

    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        //        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        Date date = null;
        date = formatter.parse(strTime);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    /**
     * 一天内
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        boolean today = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date2 = calendar.getTime();
        if (date.getTime() > date2.getTime() - 5000 || date.getTime() == date2.getTime()) {
            today = true;
        }
        return today;
    }

    /**
     * 一周内
     *
     * @param date
     * @return
     */
    public static boolean isWeek(Date date) {
        boolean today = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        Date date2 = calendar.getTime();
        if (date.getTime() > date2.getTime() - 5000 || date.getTime() == date2.getTime()) {
            today = true;
        }
        return today;
    }

    /**
     * 一个月内
     *
     * @param date
     * @return
     */
    public static boolean isMonth(Date date) {
        boolean today = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date2 = calendar.getTime();
        if (date.getTime() > date2.getTime() - 5000 || date.getTime() == date2.getTime()) {
            today = true;
        }
        return today;
    }

    /**
     * 一年内
     *
     * @param date
     * @return
     */
    public static boolean isYear(Date date) {
        boolean today = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date date2 = calendar.getTime();
        if (date.getTime() > date2.getTime() - 5000 || date.getTime() == date2.getTime()) {
            today = true;
        }
        return today;
    }
}
