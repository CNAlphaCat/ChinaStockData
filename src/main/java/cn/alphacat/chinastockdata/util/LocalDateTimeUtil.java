package cn.alphacat.chinastockdata.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeUtil {
  private static final DateTimeFormatter DATE_TIME_FORMATTER_yyyy_MM_dd_HH_mm =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_TIME_FORMATTER_HH_mm_ss =
      DateTimeFormatter.ofPattern("HH:mm:ss");

  public static LocalDateTime autoParseDateTime(String dateTime) {
    LocalDateTime dateTimeResult = parseDateTimeOfPatternyyyy_MM_dd_HH_mm(dateTime);
    if (dateTimeResult != null) {
      return dateTimeResult;
    }
    return parseTodayTimeOfPatternHH_mm_ss(dateTime);
  }

  public static LocalDateTime parseTodayTimeOfPatternHH_mm_ss(String time) {
    try {
      LocalTime localTime = LocalTime.parse(time, DATE_TIME_FORMATTER_HH_mm_ss);
      LocalDate currentDate = LocalDateUtil.getNow();
      return LocalDateTime.of(currentDate, localTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid time format: " + time, e);
    }
  }

  public static LocalDateTime parseDateTimeOfPatternyyyy_MM_dd_HH_mm(String dateTime) {
    try {
      return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER_yyyy_MM_dd_HH_mm);
    } catch (DateTimeParseException e) {
      return null;
    }
  }
}
