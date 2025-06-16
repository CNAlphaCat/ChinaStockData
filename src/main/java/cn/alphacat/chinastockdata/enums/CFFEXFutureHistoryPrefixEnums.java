package cn.alphacat.chinastockdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CFFEXFutureHistoryPrefixEnums {
  IF("IF"),
  IM("IM"),
  IC("IC");
  private String value;
}
