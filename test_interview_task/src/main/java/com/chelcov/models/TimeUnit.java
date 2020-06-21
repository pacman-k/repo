package com.chelcov.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public enum TimeUnit {
    HOUR(new SimpleDateFormat("yyyy/MM/dd HH")),
    DAY(new SimpleDateFormat("yyyy/MM/dd")),
    MONTH(new SimpleDateFormat("yyyy/MM"));

    private DateFormat dateFormat;

    TimeUnit(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public Comparator<LogRecord> getLogRecordComparator() {
        return (l1, l2) -> {
            long calendarL1TimeCode = getTimeCode(TimeUnit.getCalendarFromDate(l1.getDate()));
            long calendarL2TimeCode = getTimeCode(TimeUnit.getCalendarFromDate(l2.getDate()));
            return Long.compare(calendarL1TimeCode, calendarL2TimeCode);
        };
    }

    private static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    private long getTimeCode(Calendar calendar) {
        switch (this) {
            case HOUR:
                return calendar.get(Calendar.YEAR) * 1000000 + calendar.get(Calendar.MONTH) * 10000
                        + calendar.get(Calendar.DAY_OF_MONTH) * 100 + calendar.get(Calendar.HOUR_OF_DAY);
            case DAY:
                return calendar.get(Calendar.YEAR) * 10000 + calendar.get(Calendar.MONTH) * 100
                        + calendar.get(Calendar.DAY_OF_MONTH);
            case MONTH:
                return calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH);
            default:
                return 0;
        }
    }

}
