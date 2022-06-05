package com.cellulam.core.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 默认使用GMT+8时区, Asia/Shanghai <br>
 * <p>
 * GMT: 格林威治平均时, 中国位于东8区，因此时间是GMT+8 <br>
 * 但是, 必须使用 GMT-8, GMT-8事实上是GMT+8区, Etc/GMT-8
 * @author eric.li
 */
public abstract class LocalDateUtils {

    public static final long DEFAULT_TIMESTAMP = -1L;

    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static final DateTimeFormatter YEAR_MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 中国标准时区 GMT+8
     */
    public static final ZoneId GMT8 = ZoneId.of("Asia/Shanghai");

    /**
     * 日期时间转为毫秒数据(Epoch), 默认使用GMT+8时区
     *
     * @param dt
     * @return
     */
    public static long toTimestamp(ChronoLocalDateTime dt) {
        return toTimestamp(dt, GMT8);
    }

    /**
     * 是否为系统所认可的有效时间 <br>
     * != null && year > 1900
     *
     * @param dt
     * @return
     */
    public static boolean isValidTime(ChronoLocalDateTime dt) {
        return dt != null && dt.get(ChronoField.YEAR) > 1900;
    }

    /**
     * 是否为系统所 无效时间 <br>
     * == null || year <= 1900
     *
     * @param dt
     * @return
     */
    public static boolean isNotValidTime(ChronoLocalDateTime dt) {
        return !isValidTime(dt);
    }

    /**
     * 日期时间转为毫秒数据(Epoch), 指定时区
     *
     * @param dt
     * @param zone
     * @return
     */
    public static long toTimestamp(ChronoLocalDateTime dt, ZoneId zone) {
        return dt == null ? DEFAULT_TIMESTAMP : dt.atZone(zone).toInstant().toEpochMilli();
    }

    /**
     * 毫秒数据(Epoch)转为日期时间转, 默认使用GMT+8时区
     *
     * @param mills
     * @return
     */
    public static LocalDateTime toDateTime(long mills) {
        return toDateTime(mills, GMT8);
    }

    /**
     * 毫秒数据(Epoch)转为日期时间转, 指定时区
     *
     * @param mills
     * @param zone
     * @return
     */
    public static LocalDateTime toDateTime(long mills, ZoneId zone) {
        return mills < 0 ? null : Instant.ofEpochMilli(mills).atZone(zone).toLocalDateTime();
    }

    /**
     * 从<code>Date</code>转为LocalDateTime
     *
     * @param in
     * @return
     */
    public static LocalDateTime from(Date in) {
        return from(in, GMT8);
    }

    /**
     * 从<code>Date</code>转为LocalDateTime
     *
     * @param in
     * @param zone
     * @return
     */
    public static LocalDateTime from(Date in, ZoneId zone) {
        return in == null ? null : LocalDateTime.ofInstant(in.toInstant(), zone);
    }

    /**
     * 从<code>LocalDateTime</code>转为Date
     *
     * @param ldt
     * @return
     */
    public static Date convertToDate(LocalDateTime ldt) {
        return convertToDate(ldt, GMT8);
    }

    /**
     * 从<code>LocalDateTime</code>转为Date
     *
     * @param ldt
     * @param zone
     * @return
     */
    public static Date convertToDate(LocalDateTime ldt, ZoneId zone) {
        return ldt == null ? null : Date.from(ldt.atZone(zone).toInstant());
    }

    public static int toYearMonth(TemporalAccessor temporal) {
        if (temporal == null) {
            return 0;
        }

        return Integer.parseInt(YEAR_MONTH_FORMATTER.format(temporal));
    }


    /**
     * 时间戳转LocalDateTime
     * 使用东8区
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return toLocalDateTime(timestamp, GMT8);
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @param zoneId
     * @return
     */
    public static LocalDateTime toLocalDateTime(long timestamp, ZoneId zoneId) {
        if (timestamp == DEFAULT_TIMESTAMP) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * diff
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static long diff(ChronoLocalDateTime dt1, ChronoLocalDateTime dt2) {
        return diff(dt1, toTimestamp(dt2));
    }

    /**
     * diff
     *
     * @param dt
     * @param timestamp
     * @return
     */
    public static long diff(ChronoLocalDateTime dt, long timestamp) {
        return Math.abs(toTimestamp(dt) - timestamp);
    }
}
