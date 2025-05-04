package cn.alphacat.chinastockdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStatusEnum {
  TRADE(1),
  NOT_TRADE(0);
  private final Integer status;

  public static TradeStatusEnum fromStatus(Integer status) {
    for (TradeStatusEnum tradeStatusEnum : TradeStatusEnum.values()) {
      if (tradeStatusEnum.getStatus().equals(status)) {
        return tradeStatusEnum;
      }
    }
    return null;
  }
}
