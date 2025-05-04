package cn.alphacat.chinastockdata.model.stock;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockLimitDown {
  private Integer serialNumber;
  private String stockCode;
  private String stockName;
  private BigDecimal changePercent;
  private BigDecimal price;
  private BigDecimal currentAmount;
  private BigDecimal totalAmount;
  private BigDecimal exchangeRate;
  // 封板资金
  private BigDecimal closureFunding;
  // 首次封板时间
  private LocalDateTime firstClosureTime;
  // 最后封板时间
  private LocalDateTime lastClosureTime;
  // 炸板次数
  private BigDecimal openClosureCount;
  // 涨停统计
  private Object limitUpCount;
  // 连板数
  private Integer continuousClosureCount;
  // 所属行业
  private Object industry;
}
