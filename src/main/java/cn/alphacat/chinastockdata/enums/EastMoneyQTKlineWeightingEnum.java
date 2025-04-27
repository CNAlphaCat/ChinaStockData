package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum EastMoneyQTKlineWeightingEnum {
  NON_WEIGHTING("0", "不复权"),
  PRE_WEIGHTING("1", "前复权"),
  POST_WEIGHTING("2", "后复权");

  private final String key;
  private final String description;

  EastMoneyQTKlineWeightingEnum(String key, String description) {
    this.key = key;
    this.description = description;
  }
}
