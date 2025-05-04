package cn.alphacat.chinastockdata.model;

import cn.alphacat.chinastockdata.enums.TradeStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SZSECalendar {
  private Integer dayWeek;
  private TradeStatusEnum tradeStatus;
  private LocalDate tradeDate;
}
