package cn.alphacat.chinastockdata.model.future;

import cn.alphacat.chinastockdata.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CFFEXFutureHistory {
  private String code;
  private BigDecimal open;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private BigDecimal amount;
  private BigDecimal holdingVolume;
  private BigDecimal holdingVolumeChange;
  private BigDecimal close;
  private BigDecimal settlementToday;
  private BigDecimal settlementYesterday;
  private BigDecimal change1;
  private BigDecimal change2;
  private BigDecimal delta;
  private LocalDate date;

  public CFFEXFutureHistory(OriginalCFFEXFutureHistory originalCFFEXFutureHistory) {
    this.code = originalCFFEXFutureHistory.getCode();
    this.open = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getOpen());
    this.high = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getHigh());
    this.low = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getLow());
    this.volume = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getVolume());
    this.amount = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getAmount());
    this.holdingVolume = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getHoldingVolume());
    this.holdingVolumeChange =
        StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getHoldingVolumeChange());
    this.close = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getClose());
    this.settlementToday =
        StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getSettlementToday());
    this.settlementYesterday =
        StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getSettlementYesterday());
    this.change1 = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getChange1());
    this.change2 = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getChange2());
    this.delta = StringUtil.parseBigDecimal(originalCFFEXFutureHistory.getDelta());
  }
}
