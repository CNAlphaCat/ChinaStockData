package cn.alphacat.chinastockdata.model.future;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FutureHistory {
  private String code;
  private LocalDate date;
  private LocalDateTime dateTime;
  private BigDecimal open;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private BigDecimal amount;
  private BigDecimal amplitude;
  private BigDecimal changeRate;
  private BigDecimal changeAmount;
  private BigDecimal turnoverRate;
  private BigDecimal holdingVolume;

  public FutureHistory(CFFEXFutureHistory cffexFutureHistory){
    this.code = cffexFutureHistory.getCode();
    this.date = cffexFutureHistory.getDate();
    this.close = cffexFutureHistory.getClose();
    this.open = cffexFutureHistory.getOpen();
    this.high = cffexFutureHistory.getHigh();
    this.low = cffexFutureHistory.getLow();
    this.volume = cffexFutureHistory.getVolume();
    this.amount = cffexFutureHistory.getAmount();
    this.holdingVolume = cffexFutureHistory.getHoldingVolume();
  }
}
