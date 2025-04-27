package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum FutureHistoryEnum {
  DATE("f51", "日期"),
  OPEN_PRICE("f52", "开盘"),
  CLOSE_PRICE("f53", "收盘"),
  HIGH_PRICE("f54", "最高"),
  LOW_PRICE("f55", "最低"),
  VOLUME("f56", "成交量"),
  TURNOVER("f57", "成交额"),
  AMPLITUDE("f58", "振幅"),
  CHANGE_PERCENTAGE("f59", "涨跌幅"),
  CHANGE_AMOUNT("f60", "涨跌额"),
  TURNOVER_RATE("f61", "换手率");

  private final String key;
  private final String description;

  FutureHistoryEnum(String key, String description) {
    this.key = key;
    this.description = description;
  }
}
