package cn.alphacat.chinastockdata.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateUtil {
  private static final DateTimeFormatter DATE_FORMATTER_yyyyMMdd =
      DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd_HH_mm_ss =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd_HH_mm =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static LocalDate getNow() {
    return LocalDate.now(ZoneId.of("Asia/Shanghai"));
  }

  public static LocalDate autoParseDate(String date) {
    LocalDate result = parseDateOfPatternyyyy_MM_dd(date);
    if (result != null) {
      return result;
    }
    result = parseDateOfPatternyyyy_MM_dd_HH_mm_ss(date);
    if (result != null) {
      return result;
    }
    result = parseDateOfPatternyyyy_MM_dd_HH_mm(date);
    if (result != null) {
      return result;
    }
    result = parseDateOfPatternyyyyMMdd(date);
    if (result != null) {
      return result;
    }
    return null;
  }

  public static LocalDate parseDateOfPatternyyyy_MM_dd_HH_mm(String date) {
    try {
      return LocalDate.parse(date, DATE_FORMATTER_yyyy_MM_dd_HH_mm);
    } catch (Exception e) {
      return null;
    }
  }

  public static LocalDate parseDateOfPatternyyyy_MM_dd_HH_mm_ss(String date) {
    try {
      return LocalDate.parse(date, DATE_FORMATTER_yyyy_MM_dd_HH_mm_ss);
    } catch (Exception e) {
      return null;
    }
  }

  public static LocalDate parseDateOfPatternyyyy_MM_dd(String date) {
    try {
      return LocalDate.parse(date, DATE_FORMATTER_yyyy_MM_dd);
    } catch (Exception e) {
      return null;
    }
  }

  public static LocalDate parseTimestamp(long timestamp) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    LocalDate date = instant.atZone(ZoneId.of("UTC")).toLocalDate();
    return date;
  }

  public static LocalDate parseDateOfPatternyyyyMMdd(String date) {
    try {
      return LocalDate.parse(date, DATE_FORMATTER_yyyyMMdd);
    } catch (Exception e) {
      return null;
    }
  }


  public static LocalDate parseDateOfPatternyyyyMMdd(Date date) {
    try {
      return LocalDate.parse(DATE_FORMATTER_yyyyMMdd.format(date.toInstant()));
    } catch (Exception e) {
      return null;
    }
  }
}
