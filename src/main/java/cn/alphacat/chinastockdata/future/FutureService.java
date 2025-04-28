package cn.alphacat.chinastockdata.future;

import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.model.FutureDetail;
import cn.alphacat.chinastockdata.model.FutureHistory;
import cn.alphacat.chinastockdata.model.FutureMarketOverview;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FutureService {
  private final FutureBaseInfoHandler futureBaseInfoHandler;
  private final FutureHistoryHandler futureHistoryHandler;
  private final FutureDetailHandler futureDetailHandler;

  public FutureService(
      final FutureBaseInfoHandler futureBaseInfoHandler,
      final FutureHistoryHandler futureHistoryHandler,
      final FutureDetailHandler futureDetailHandler) {
    this.futureBaseInfoHandler = futureBaseInfoHandler;
    this.futureHistoryHandler = futureHistoryHandler;
    this.futureDetailHandler = futureDetailHandler;
  }

  public List<FutureMarketOverview> getFuturesBaseInfo() {
    return futureBaseInfoHandler.getFuturesBaseInfo();
  }

  public List<FutureHistory> getFutureHistory(
      String code,
      LocalDate beginDate,
      LocalDate endDate,
      EastMoneyQTKlineTypeEnum klt,
      EastMoneyQTKlineWeightingEnum fqt) {
    return futureHistoryHandler.getFutureHistory(code, beginDate, endDate, klt, fqt);
  }

  public List<FutureDetail> getFutureDetails(String secId, int maxCount) {
    return futureDetailHandler.getFutureDetails(secId, maxCount);
  }
}
