package com.cellulam.core.json;

import com.cellulam.core.utils.LocalDateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 兼容DotNet时间<code></code>, <br>
 *     <code>/Date(1508428800000-0000)/</code>
 *
 */
public class LocalDateJsonDeserializer extends LocalDateDeserializer {

    public LocalDateJsonDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();
        if (text.startsWith("/Date(")) {
            String numText = text.substring(6, text.length() - 7);
            String offNum = text.substring(text.length() - 7, text.length() - 2);
            if (!numText.startsWith("-")) {
                ZoneId zoneId = null;
                if (offNum.endsWith("0000")) {
                    // 传过来的时间是GMT8时间
                    zoneId = LocalDateUtils.GMT8;
                } else {
                    zoneId = ZoneId.of(offNum);
                }
                LocalDateTime ldt = LocalDateUtils.toDateTime(Long.valueOf(numText), zoneId);
                return ldt == null ? null : ldt.toLocalDate();
            } else {
                return null;
            }
        } else {
            return super.deserialize(p, ctxt);
        }
    }

}
