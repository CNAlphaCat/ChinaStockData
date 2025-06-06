package cn.alphacat.chinastockdata.model.stock;

import cn.alphacat.chinastockdata.enums.StockExchangeMarketEnums;
import lombok.Data;

@Data
public class StockInfo {
  private String stockCode;
  private String stockName;
  private StockExchangeMarketEnums exchangeMarket;
}
