package com.v2p.swp391.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static long getDiffDay(Date source, Date target){
        LocalDate ldSource = LocalDate.from(source.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        LocalDate ldTarget = LocalDate.from(target.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        Duration nights = Duration.between(ldTarget.atStartOfDay(), ldSource.atStartOfDay());

        return nights.toDays();
    }

    public static LocalDateTime parseTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(timestamp, formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
