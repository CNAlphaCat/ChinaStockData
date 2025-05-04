package cn.alphacat.chinastockdata.model.marketindex;

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

  public boolean checkValid() {
    if (this.indexCode == null || this.indexCode.isEmpty()) {
      return false;
    }
    if (this.tradeDate == null) {
      return false;
    }
    if (this.open == null) {
      return false;
    }
    if (this.high == null) {
      return false;
    }
    if (this.low == null) {
      return false;
    }
    if (this.close == null) {
      return false;
    }
    if (this.volume == null) {
      return false;
    }
    if (this.amount == null) {
      return false;
    }
    if (this.change == null) {
      return false;
    }
    if (this.changePct == null) {
      return false;
    }
    return true;
  }
}
