package cn.alphacat.chinastockdata.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {
  private static final DateTimeFormatter DATE_FORMATTER_yyyy_MM_dd =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static LocalDate parseDateOfPatternyyyy_MM_dd(String date) {
    return LocalDate.parse(date, DATE_FORMATTER_yyyy_MM_dd);
  }
}
