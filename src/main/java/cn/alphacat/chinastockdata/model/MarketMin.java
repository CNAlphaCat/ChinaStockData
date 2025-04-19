package cn.alphacat.chinastockdata.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MarketMin {
  private String stockCode;
  private LocalDateTime tradeTime;
  private BigDecimal price;
  private BigDecimal change;
  private BigDecimal changePercent;
  private BigDecimal volume;
  private BigDecimal averagePrice;
  private BigDecimal amount;
}
