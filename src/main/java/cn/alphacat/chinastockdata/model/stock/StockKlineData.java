package cn.alphacat.chinastockdata.model.stock;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StockKlineData {
  private String stockCode;
  private String stockName;
  private Integer decimal;
  private BigDecimal preKPrice;
  private List<StockKline> kLines;
}
