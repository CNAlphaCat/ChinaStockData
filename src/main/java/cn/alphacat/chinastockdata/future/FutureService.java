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
import java.util.ArrayList;
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

  public HashMap<LocalDate, List<CFFEXFutureHistory>> getStockFutureHistory(
          Year year, Month month) {
    List<CFFEXFutureHistoryPrefixEnums> prefixEnums =
            List.of(
                    CFFEXFutureHistoryPrefixEnums.IF,
                    CFFEXFutureHistoryPrefixEnums.IM,
                    CFFEXFutureHistoryPrefixEnums.IC);
    return getFutureHistory(year, month, prefixEnums);
  }

  public HashMap<LocalDate, List<CFFEXFutureHistory>> getFutureHistory(
      Year year, Month month, List<CFFEXFutureHistoryPrefixEnums> prefixEnums) {
    return cffexFutureHistoryHandler.getFutureHistory(year, month, prefixEnums);
  }

  public HashMap<LocalDate, List<CFFEXFutureHistory>> getStockFutureHistory(
          int startYear, Month startMonth) {
    LocalDate currentDate = LocalDate.of(startYear, startMonth, 1);
    LocalDate endLocalDate = LocalDate.now().plusMonths(1);
    Month endMonth = endLocalDate.getMonth();
    LocalDate endDate = LocalDate.of(endLocalDate.getYear(), endMonth, 1);
    HashMap<LocalDate, List<CFFEXFutureHistory>> result = new HashMap<>();
    while (currentDate.isBefore(endDate)) {
      Year currentYear = Year.of(currentDate.getYear());
      Month currentMonth = Month.of(currentDate.getMonthValue());
      HashMap<LocalDate, List<CFFEXFutureHistory>> futureHistory =
              getStockFutureHistory(currentYear, currentMonth);
      futureHistory.forEach(
              (date, futures) -> {
                if (result.containsKey(date)) {
                  result.get(date).addAll(futures);
                } else {
                  result.put(date, new ArrayList<>(futures));
                }
              });
      currentDate = currentDate.plusMonths(1);
    }
    return result;
  }
}
