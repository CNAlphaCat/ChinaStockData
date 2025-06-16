package cn.alphacat.chinastockdata.model.future;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OriginalCFFEXFutureHistory {
  private String code;
  private String open;
  private String high;
  private String low;
  private String volume;
  private String amount;
  private String holdingVolume;
  private String holdingVolumeChange;
  private String close;
  private String settlementToday;
  private String settlementYesterday;
  private String change1;
  private String change2;
  private String delta;
}
