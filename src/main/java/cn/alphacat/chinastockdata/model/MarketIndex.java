package cn.alphacat.chinastockdata.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MarketIndex {
  private String indexCode;
  private LocalDate tradeDate;
  private LocalDateTime tradeTime;
  private BigDecimal open;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal close;
  private BigDecimal volume;
  private BigDecimal amount;
  private BigDecimal change;
  private BigDecimal changePct;
}
