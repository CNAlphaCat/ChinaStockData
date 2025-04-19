package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.StockMin;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
  private final EastMoneyStockService eastMoneyStockService;

  public StockService(final EastMoneyStockService eastMoneyStockService) {
    this.eastMoneyStockService = eastMoneyStockService;
  }

  /** 获取单个股票的今日分时行情 */
  public ArrayList<StockMin> getMarketMin(String stockCode) {
    if (isBeforeStockOpenTime()) {
      return new ArrayList<>();
    }
    ArrayList<StockMin> stockMin = eastMoneyStockService.buildMarketMin(stockCode);
    if (stockMin != null && !stockMin.isEmpty()) {
      return stockMin;
    }
    return null;
  }

  private Boolean isBeforeStockOpenTime() {
    LocalTime now = LocalTime.now();
    LocalTime openTime = LocalTime.of(9, 15);
    return now.isBefore(openTime);
  }
}
