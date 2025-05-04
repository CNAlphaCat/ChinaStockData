package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.stock.StockMin;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Service
public class StockService {
  private final EastMoneyStockService eastMoneyStockService;
  private final BaiduStockService baiduStockService;

  public StockService(
      final EastMoneyStockService eastMoneyStockService,
      final BaiduStockService baiduStockService) {
    this.eastMoneyStockService = eastMoneyStockService;
    this.baiduStockService = baiduStockService;
  }

  /** 获取单个股票的今日分时行情 */
  public ArrayList<StockMin> getStockMin(String stockCode) {
    if (stockCode == null || stockCode.isEmpty()) {
      return null;
    }
    if (isBeforeStockOpenTime()) {
      return new ArrayList<>();
    }
    ArrayList<StockMin> stockMin = eastMoneyStockService.getStockMin(stockCode);
    if (stockMin != null && !stockMin.isEmpty()) {
      return stockMin;
    }
    return baiduStockService.getStockMin(stockCode);
  }

  private Boolean isBeforeStockOpenTime() {
    LocalTime now = LocalTime.now(ZoneId.of("Asia/Shanghai"));;
    LocalTime openTime = LocalTime.of(9, 15);
    return now.isBefore(openTime);
  }
}
