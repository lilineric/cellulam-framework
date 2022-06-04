package com.cellulam.core.json;

import com.cellulam.core.utils.LocalDateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 兼容时间<code> /Date(1508053827526+0800)/ </code>, <br>
 *     <code>/Date(-62135596800000-0000)/</code>
 *
 */
public class LocalDateTimeJsonDeserializer extends LocalDateTimeDeserializer {

    private static final Pattern donetLongFormat = Pattern.compile("(\\d{4})[-/](\\d{1,2})[-/](\\d{1,2})[T ](\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\.?(\\d+)?\\+\\d{2}:?\\d{2}");
    private static final Pattern middleFormat = Pattern.compile("(\\d{4})[-/](\\d{1,2})[-/](\\d{1,2})[T ](\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\.?(\\d+)?");
    private static final Pattern dateFormat = Pattern.compile("(\\d{4})[-/](\\d{1,2})[-/](\\d{1,2})");

    // MM/dd/yyyy hh:mm:ss a
    private static final Pattern monthDayYear = Pattern.compile("(\\d{1,2})[/](\\d{1,2})[/](\\d{4})[T ](\\d{1,2}):(\\d{1,2}):(\\d{1,2}) ([AMP]{2})");

    private static final List<Pattern> specialFormats = Lists.newArrayList();

    public LocalDateTimeJsonDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

//        ctxt.get

        String text = p.getText();
        if (text.startsWith("/Date(")) {
            String numText = text.substring(6, text.length() - 7);
            String offNum = text.substring(text.length() - 7, text.length() - 2);
            if (!numText.startsWith("-")) {
                return LocalDateUtils.toDateTime(Long.valueOf(numText), ZoneId.of(offNum));
            } else {
                return null;
            }
        } else {
            Matcher m = donetLongFormat.matcher(text);
            if (m.matches()) {
                return getLocalDateTime(m);
            } else if ( (m = middleFormat.matcher(text)).matches() ) {
                return getLocalDateTime(m);
            } else if ((m = dateFormat.matcher(text)).matches()) {
                return getLocalDate(m);
            } else if ((m = monthDayYear.matcher(text)).matches()) {
                String ampm = m.group(7);
                int addedHour = 0;
                if ("am".equalsIgnoreCase(ampm)) {

                } else if ("pm".equalsIgnoreCase(ampm)) {
                    addedHour = 12;
                }
                return LocalDateTime.of(Integer.valueOf(m.group(3)), Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)),
                        Integer.valueOf(m.group(4)) + addedHour, Integer.valueOf(m.group(5)), Integer.valueOf(m.group(6)), 0);
            } else {
                return super.deserialize(p, ctxt);
            }

        }
    }

    private LocalDateTime getLocalDateTime(Matcher m) {
        int nano = 0;
        int sec = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        try {
            if (m.groupCount() >= 7) {
                nano = stringToInt(StringUtils.rightPad(m.group(7), 9, "0"));
            }
            if (m.groupCount() >= 6) {
                sec = stringToInt(m.group(6));
            }
            if (m.groupCount() >= 5) {
                minutes = stringToInt(m.group(5));
            }
            if (m.groupCount() >= 4) {
                hours = stringToInt(m.group(4));
            }
            if (m.groupCount() >= 3) {
                days = stringToInt(m.group(3));
            }
        } catch (Exception e) {
            nano = 0;
        }
        return LocalDateTime.of(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)), days,
                hours, minutes, sec, nano);
    }

    private int stringToInt(String text) {
        return StringUtils.isEmpty(text) ? 0 : Integer.valueOf(text);
    }

    private LocalDateTime getLocalDate(Matcher m) {
        return LocalDateTime.of(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)),
                0, 0, 0, 0);
    }

}
