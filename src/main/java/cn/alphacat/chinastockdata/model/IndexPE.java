package cn.alphacat.chinastockdata.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class IndexPE {
  private String indexCode;
  private LocalDate date;
  // 指数
  private BigDecimal close;
  // 等权静态市盈率
  private BigDecimal lyrPe;
  // 静态市盈率
  private BigDecimal addLyrPe;
  // 静态市盈率中位数
  private BigDecimal middleLyrPe;
  // 等权滚动市盈率
  private BigDecimal ttmPe;
  // 滚动市盈率
  private BigDecimal addTtmPe;
  // 滚动市盈率中位数
  private BigDecimal middleTtmPe;
}
