package cn.alphacat.chinastockdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StockExchangeMarketEnums {
  SHENZHEN("SZ"),
  SHANGHAI("SH"),
  BEIJING("BJ");

  private String value;

  public static StockExchangeMarketEnums getStockExchangeMarketEnums(String value) {
    for (StockExchangeMarketEnums stockExchangeMarketEnums : StockExchangeMarketEnums.values()) {
      if (stockExchangeMarketEnums.getValue().equals(value)) {
        return stockExchangeMarketEnums;
      }
    }
    return null;
  }
}
