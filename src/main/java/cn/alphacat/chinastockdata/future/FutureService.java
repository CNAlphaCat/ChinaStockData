package cn.alphacat.chinastockdata.future;

import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.model.FutureHistory;
import cn.alphacat.chinastockdata.model.FutureMarketOverview;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FutureService {
  private final FutureBaseInfoHandler futureBaseInfoHandler;
  private final FutureHistoryHandler futureHistoryHandler;

  public FutureService(
      final FutureBaseInfoHandler futureBaseInfoHandler,
      final FutureHistoryHandler futureHistoryHandler) {
    this.futureBaseInfoHandler = futureBaseInfoHandler;
    this.futureHistoryHandler = futureHistoryHandler;
  }

  public List<FutureMarketOverview> getFuturesBaseInfo() {
    return futureBaseInfoHandler.getFuturesBaseInfo();
  }

  public List<FutureHistory> getFutureHistory(
      String code,
      LocalDate beg,
      LocalDate end,
      EastMoneyQTKlineTypeEnum klt,
      EastMoneyQTKlineWeightingEnum fqt) {
    return futureHistoryHandler.getFutureHistory(code, beg, end, klt, fqt);
  }
}
