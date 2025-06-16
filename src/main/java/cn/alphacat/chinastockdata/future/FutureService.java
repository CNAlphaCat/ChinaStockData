package cn.alphacat.chinastockdata.future;

import cn.alphacat.chinastockdata.enums.CFFEXFutureHistoryPrefixEnums;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.future.handler.CFFEXFutureHistoryHandler;
import cn.alphacat.chinastockdata.future.handler.EastMoneyFutureHistoryHandler;
import cn.alphacat.chinastockdata.future.handler.FutureBaseInfoHandler;
import cn.alphacat.chinastockdata.future.handler.FutureDetailHandler;
import cn.alphacat.chinastockdata.model.future.CFFEXFutureHistory;
import cn.alphacat.chinastockdata.model.future.FutureDetail;
import cn.alphacat.chinastockdata.model.future.FutureHistory;
import cn.alphacat.chinastockdata.model.future.FutureMarketOverview;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;

@Service
public class FutureService {
  private final FutureBaseInfoHandler futureBaseInfoHandler;
  private final EastMoneyFutureHistoryHandler eastMoneyFutureHistoryHandler;
  private final FutureDetailHandler futureDetailHandler;
  private final CFFEXFutureHistoryHandler cffexFutureHistoryHandler;

  public FutureService(
      final FutureBaseInfoHandler futureBaseInfoHandler,
      final EastMoneyFutureHistoryHandler eastMoneyFutureHistoryHandler,
      final FutureDetailHandler futureDetailHandler,
      final CFFEXFutureHistoryHandler cffexFutureHistoryHandler) {
    this.futureBaseInfoHandler = futureBaseInfoHandler;
    this.eastMoneyFutureHistoryHandler = eastMoneyFutureHistoryHandler;
    this.futureDetailHandler = futureDetailHandler;
    this.cffexFutureHistoryHandler = cffexFutureHistoryHandler;
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
    return eastMoneyFutureHistoryHandler.getFutureHistory(code, beginDate, endDate, klt, fqt);
  }

  public List<FutureDetail> getFutureDetails(String secId, int maxCount) {
    return futureDetailHandler.getFutureDetails(secId, maxCount);
  }

  public HashMap<LocalDate, List<CFFEXFutureHistory>> getFutureHistory(
      Year year, Month month, List<CFFEXFutureHistoryPrefixEnums> prefixEnums) {
    return cffexFutureHistoryHandler.getFutureHistory(year, month, prefixEnums);
  }
}
