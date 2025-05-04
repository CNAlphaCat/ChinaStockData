package cn.alphacat.chinastockdata.model.stock;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockMin {
  private String stockCode;
  private LocalDateTime tradeTime;
  private BigDecimal openPrice;
  private BigDecimal closePrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal change;
  private BigDecimal changePercent;
  private BigDecimal volume;
  private BigDecimal averagePrice;
  private BigDecimal amount;
}
