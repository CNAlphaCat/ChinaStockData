package cn.alphacat.chinastockdata.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FutureHistory {
  private LocalDate date;
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
}
