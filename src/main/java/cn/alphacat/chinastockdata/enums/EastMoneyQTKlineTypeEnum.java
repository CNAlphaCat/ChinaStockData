package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum EastMoneyQTKlineTypeEnum {
  ONE_MINUTE("1", "1分钟"),
  FIVE_MINUTE("5", "5分钟"),
  FIFTEEN_MINUTE("15", "15分钟"),
  THIRTY_MINUTE("30", "30分钟"),
  SIXTY_MINUTE("60", "60分钟"),
  DAILY("101", "日线"),
  WEEKLY("102", "周线"),
  MONTHLY("103", "月线");

  private final String key;
  private final String description;

  EastMoneyQTKlineTypeEnum(String key, String description) {
    this.key = key;
    this.description = description;
  }
}
