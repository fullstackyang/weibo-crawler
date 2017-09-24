package com.fullstackyang.crawler.weibo.utils;

import com.google.common.base.Strings;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class DateConvertor {

    private static final String PATTERN_1 = "^刚刚$";
    private static final String PATTERN_2 = "^\\d{1,2}秒前$";
    private static final String PATTERN_3 = "^\\d{1,2}分钟前$";
    private static final String PATTERN_4 = "^\\d{1,2}小时前$";
    private static final String PATTERN_5 = "^\\d+天前$";
    private static final String PATTERN_6 = "^\\d+月前$";
    private static final String PATTERN_7 = "^\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2}$";
    private static final String PATTERN_8 = "^\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2}(\\s+)\\d{1,2}:\\d{1,2}(:\\d{1,2})?$";
    private static final String PATTERN_9 = "^[今|昨]天(\\s*)\\d{1,2}:\\d{1,2}(:\\d{1,2})?$";

    private static final Map<Integer, String> map = new HashMap<Integer, String>() {
        {
            put(1, PATTERN_1);
            put(2, PATTERN_2);
            put(3, PATTERN_3);
            put(4, PATTERN_4);
            put(5, PATTERN_5);
            put(6, PATTERN_6);
            put(7, PATTERN_7);
            put(8, PATTERN_8);
            put(9, PATTERN_9);
        }
    };

    public static LocalDateTime convert(String date) {
        return convert(date, LocalDateTime.now());
    }

    public static LocalDateTime convert(final String dateTimeStr, LocalDateTime dateTime) {
        if (Strings.isNullOrEmpty(dateTimeStr))
            return dateTime;
        String temp = dateTimeStr.trim().replaceAll("\\s{2,}", " ").replaceAll("年|/", "-");
        if (dateTimeStr.contains("月") && dateTimeStr.contains("日")) {
            temp = temp.replace("月", "-").replace("日", "");
            if (!dateTimeStr.contains("年"))
                temp = LocalDateTime.now().getYear() + "-" + temp;
        }

        for (Entry<Integer, String> entry : map.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            Matcher matcher = Pattern.compile(value).matcher(temp);
            if (matcher.find()) {
                return calculate(key, temp, dateTime);
            }
        }
        return dateTime;
    }

    private static LocalDateTime calculate(int key, String dateTimeStr, LocalDateTime dateTime) {
        switch (key) {
            case 1:// "^刚刚$";
                break;
            case 2:// "^\\d{1,2}秒前$"
                return dateTime.minusSeconds(Long.parseLong(dateTimeStr.replace("秒前", "")));
            case 3:// "^\\d{1,2}分钟前$"
                return dateTime.minusMinutes(Long.parseLong(dateTimeStr.replace("分钟前", "")));
            case 4:// "^\\d{1,2}小时前$"
                return dateTime.minusHours(Long.parseLong(dateTimeStr.replace("小时前", "")));
            case 5:// "^\\d+天前$";
                return dateTime.minusDays(Long.parseLong(dateTimeStr.replace("天前", "")));
            case 6:// "^\\d+月前$"
                return dateTime.minusMonths(Long.parseLong(dateTimeStr.replace("月前", "")));
            case 7:// "^\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2}$";
                return LocalDateTime.parse(padding(dateTimeStr, "-"));
            case 8:// "^\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2}(\\s+)\\d{1,2}:\\d{1,2}(:\\d{1,2})?$"
                String[] parts = dateTimeStr.split(" ");
                return LocalDateTime.parse(padding(parts[0], "-") + "T" + padding(parts[1], ":"));
            case 9:// "^[今|昨]天(\\s*)\\d{2}:\\d{2}$"
                String time = padding(dateTimeStr.replaceAll("[今|昨]天(\\s*)", ""), ":");
                if (dateTimeStr.contains("昨"))
                    return dateTime.toLocalDate().minusDays(1).atTime(LocalTime.parse(time));
                else return dateTime.toLocalDate().atTime(LocalTime.parse(time));
            default:
                break;
        }
        return dateTime;
    }

    private static String padding(String dateTimeStr, String delimiter) {
        List<String> list = Arrays.stream(dateTimeStr.split(delimiter)).map(s -> Strings.padStart(s, 2, '0')).collect(toList());
        return String.join(delimiter, list);
    }

}
