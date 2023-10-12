package com.v2p.swp391.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public static Date parseTimestamp(String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return dateFormat.parse(timestamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
