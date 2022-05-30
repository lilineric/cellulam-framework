package com.cellulam.core.utils.test;

import com.cellulam.core.utils.LocalDateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtilTest {

    @Test
    public void testTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        long timestamp = LocalDateUtils.toTimestamp(localDateTime);
        System.out.println(timestamp);
        LocalDateTime localDateTime2 = LocalDateUtils.toLocalDateTime(timestamp);
        System.out.println(localDateTime2);

        LocalDateTime dt = LocalDateTime.parse("2022-05-29 17:48:21",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Assert.assertEquals(1653817701000L,
                LocalDateUtils.toTimestamp(dt));
        Assert.assertTrue(dt.equals(LocalDateUtils.toLocalDateTime(1653817701000L)));
    }
}
