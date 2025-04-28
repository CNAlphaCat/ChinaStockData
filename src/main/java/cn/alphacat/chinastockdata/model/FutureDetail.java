package cn.alphacat.chinastockdata.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FutureDetail {
  private LocalDateTime tradeTime;
  private BigDecimal price;
  private BigDecimal volume;
  private BigDecimal tradeCount;
}
