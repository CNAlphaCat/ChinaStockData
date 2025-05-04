package cn.alphacat.chinastockdata.model.marketindex;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MarketIndexData {
  private String code;
  private Integer market;
  private String name;
  private Integer decimal;
  private BigDecimal dktotal;
  private BigDecimal preKPrice;
  private List<String> klines;
}
