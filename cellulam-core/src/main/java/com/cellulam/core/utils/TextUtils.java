package com.cellulam.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author eric.li
 * @date 2022-06-06 17:01
 */
public abstract class TextUtils {

    private static final char BRACE_START = '{';

    private static final char BRACE_END = '}';

    public static final String randomStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static final <E> String join(Collection<E> collection) {
        return join(",", collection);
    }

    public static final <E> String join(CharSequence delimiter, Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }

        return collection.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining(delimiter));
    }


    public static String compress(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != '\r'
                    && c != '\n'
                    && c != '\t'
                    && c != ' ') {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
