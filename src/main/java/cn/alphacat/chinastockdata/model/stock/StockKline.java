package cn.alphacat.chinastockdata.model.stock;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StockKline {
  private LocalDateTime dateTime;
  private LocalDate date;
  private BigDecimal open;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private BigDecimal amount;
  private BigDecimal change;
  private BigDecimal changePercent;
  private BigDecimal turnoverRatio;
}
