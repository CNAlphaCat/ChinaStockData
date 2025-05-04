package cn.alphacat.chinastockdata.model.stock;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StockLimitUpSummary {
  private LocalDate tradeDate;
  private Integer limitUpCount;
}
