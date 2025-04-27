package cn.alphacat.chinastockdata.enums;

import lombok.Getter;

@Getter
public enum EastMoneyQuoteFieldEnum {
  CODE("f12", "代码"),
  NAME("f14", "名称"),
  CHANGE_RATE("f3", "涨跌幅"),
  LATEST_PRICE("f2", "最新价"),
  HIGHEST("f15", "最高"),
  LOWEST("f16", "最低"),
  OPEN_PRICE("f17", "今开"),
  CHANGE_AMOUNT("f4", "涨跌额"),
  TURN_OVER_RATE("f8", "换手率"),
  VOLUME_RATIO("f10", "量比"),
  DYNAMIC_PE("f9", "动态市盈率"),
  VOLUME("f5", "成交量"),
  TURN_OVER("f6", "成交额"),
  PREVIOUS_CLOSE("f18", "昨日收盘"),
  TOTAL_MARKET_CAP("f20", "总市值"),
  CIRCULATING_MARKET_CAP("f21", "流通市值"),
  MARKET_CODE("f13", "市场编号"),
  UPDATE_TIMESTAMP("f124", "更新时间戳"),
  LATEST_TRADE_DATE("f297", "最新交易日");

  private final String key;
  private final String description;

  EastMoneyQuoteFieldEnum(String key, String description) {
    this.key = key;
    this.description = description;
  }
}
