package com.fullstackyang.crawler.weibo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeToString {

    private static final Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    public static String convert(String str) {
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}
