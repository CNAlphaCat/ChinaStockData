package cn.alphacat.chinastockdata.model;

import cn.alphacat.chinastockdata.model.marketindex.MarketIndexData;
import lombok.Data;

@Data
public class MarketIndexResponse {
  private MarketIndexData data;
}
