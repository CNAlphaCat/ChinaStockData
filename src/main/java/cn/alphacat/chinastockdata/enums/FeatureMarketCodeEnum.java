package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum FeatureMarketCodeEnum {
  SZ_A("0", "深A"),
  SH_A("1", "沪A"),
  SH_FUTURES("113", "上期所"),
  DCE("114", "大商所"),
  CZCE("115", "郑商所"),
  CFFEX("8", "中金所"),
  SH_ENERGY_FUTURES("142", "上海能源期货交易所");

  private final String code;
  private final String description;

  FeatureMarketCodeEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static FeatureMarketCodeEnum fromCode(String code) {
    for (FeatureMarketCodeEnum marketCodeEnum : FeatureMarketCodeEnum.values()) {
      if (marketCodeEnum.getCode().equals(code)) {
        return marketCodeEnum;
      }
    }
    return null;
  }

  public static String getDescriptionByCode(String code) {
    FeatureMarketCodeEnum featureMarketCodeEnum = fromCode(code);
    if (featureMarketCodeEnum == null) {
      return null;
    }
    return featureMarketCodeEnum.getDescription();
  }
}
