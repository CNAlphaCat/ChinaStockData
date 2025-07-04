package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.model.stock.StockInfo;
import cn.alphacat.chinastockdata.model.stock.StockKlineData;
import cn.alphacat.chinastockdata.model.stock.StockMin;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
  private final EastMoneyStockMinHandler eastMoneyStockMinHandler;
  private final BaiduStockMinHandler baiduStockMinHandler;
  private final EastMoneyStockKlineHandler eastMoneyStockKlineHandler;
  private final BaiduStockInfoHandler baiduStockInfoHandler;

  public StockService(
      final EastMoneyStockMinHandler eastMoneyStockMinHandler,
      final BaiduStockMinHandler baiduStockMinHandler,
      final EastMoneyStockKlineHandler eastMoneyStockKlineHandler,
      final BaiduStockInfoHandler baiduStockInfoHandler) {
    this.eastMoneyStockMinHandler = eastMoneyStockMinHandler;
    this.baiduStockMinHandler = baiduStockMinHandler;
    this.eastMoneyStockKlineHandler = eastMoneyStockKlineHandler;
    this.baiduStockInfoHandler = baiduStockInfoHandler;
  }

  /** 获取单个股票的今日分时行情 */
  public ArrayList<StockMin> getStockMin(String stockCode) {
    if (stockCode == null || stockCode.isEmpty()) {
      return null;
    }
    if (isBeforeStockOpenTime()) {
      return new ArrayList<>();
    }
    ArrayList<StockMin> stockMin = eastMoneyStockMinHandler.getStockMin(stockCode);
    if (stockMin != null && !stockMin.isEmpty()) {
      return stockMin;
    }
    return baiduStockMinHandler.getStockMin(stockCode);
  }

  public StockKlineData getStockKlineData(
      String stockCode, EastMoneyQTKlineTypeEnum kLineTypeEnum, LocalDate startDate) {
    return eastMoneyStockKlineHandler.getStockKlineData(stockCode, kLineTypeEnum, startDate);
  }

  public List<StockInfo> getStockInfoList() {
    return baiduStockInfoHandler.getStockInfoList();
  }

  private Boolean isBeforeStockOpenTime() {
    LocalTime now = LocalTime.now(ZoneId.of("Asia/Shanghai"));
    LocalTime openTime = LocalTime.of(9, 15);
    return now.isBefore(openTime);
  }
}
