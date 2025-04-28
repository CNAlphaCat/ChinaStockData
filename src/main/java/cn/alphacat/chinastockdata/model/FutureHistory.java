package cn.alphacat.chinastockdata.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FutureHistory {
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
}
