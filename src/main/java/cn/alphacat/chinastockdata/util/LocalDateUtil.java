package cn.alphacat.chinastockdata.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd_HH_mm_ss =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd_HH_mm =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
}
