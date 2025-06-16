package cn.alphacat.chinastockdata.util;

import java.math.BigDecimal;

public class StringUtil {
  public static BigDecimal parseBigDecimal(String str) {
    try {
      return new BigDecimal(str);
    } catch (Exception e) {
      return null;
    }
  }
}
