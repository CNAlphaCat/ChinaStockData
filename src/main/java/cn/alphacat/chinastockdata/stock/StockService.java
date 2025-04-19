package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.MarketMin;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class StockService {
  private final EastMoneyStockService eastMoneyStockService;

  public StockService(final EastMoneyStockService eastMoneyStockService) {
    this.eastMoneyStockService = eastMoneyStockService;
  }

  /** 获取单个股票的今日分时行情 */
  public List<MarketMin> getMarketMin(String stockCode) {
    if (isBeforeStockOpenTime()) {
      return null;
    }
    List<MarketMin> marketMin = eastMoneyStockService.buildMarketMin(stockCode);
    if (marketMin != null && !marketMin.isEmpty()) {
      return marketMin;
    }
    return null;
  }

  private Boolean isBeforeStockOpenTime() {
    LocalTime now = LocalTime.now();
    LocalTime openTime = LocalTime.of(9, 15);
    return now.isBefore(openTime);
  }
}
