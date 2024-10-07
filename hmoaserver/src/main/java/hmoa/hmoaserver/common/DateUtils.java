package hmoa.hmoaserver.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateUtils {
    public static String calculateDaysAgo(LocalDateTime past){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(past,now);
        long daysAgo = duration.toDays();

        if (daysAgo == 0){
            return "오늘";
        }else if (daysAgo == 1){
            return "어제";
        }else if (daysAgo<365){
            return daysAgo + "일 전";
        }else {
            long yearsAgo = daysAgo / 365;
            return yearsAgo + "년 전";
        }
    }

    public static String extractDate(LocalDateTime createAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

        return createAt.format(formatter);
    }

    public static String extractAlarmDate(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd HH:mm");

        return createdAt.format(formatter);
    }

    public static String extractOrderDate(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        return createdAt.format(formatter);
    }

    public static String extractOrderSelectDate(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return createdAt.format(formatter);
    }

    public static String extractUTC(LocalDateTime createdAt) {
        return createdAt.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
}
