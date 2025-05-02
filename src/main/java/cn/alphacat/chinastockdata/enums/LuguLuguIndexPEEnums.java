package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum LuguLuguIndexPEEnums {
  SHANGZHENG_50("上证50", "000016.SH"),
  SCI300("沪深300", "000300.SH"),
  SHANGZHENG_380("上证380", "000009.SH"),
  CHUANGYE_50("创业板50", "399673.SZ"),
  CSI500("中证500", "000905.SH"),
  SHANGZHENG_180("上证180", "000010.SH"),
  SHENZHENG_HONGLI("深证红利", "399324.SZ"),
  SHENZHENG_100("深证100", "399330.SZ"),
  CSI1000("中证1000", "000852.SH"),
  SHANGZHENG_HONGLI("上证红利", "000015.SH"),
  CSI100("中证100", "000903.SH"),
  CSI800("中证800", "000906.SH");

  private final String key;
  private final String indeCode;

  LuguLuguIndexPEEnums(String key, String indeCode) {
    this.key = key;
    this.indeCode = indeCode;
  }
}
