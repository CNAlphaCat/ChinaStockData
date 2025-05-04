package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.KLineTypeEnum;
import cn.alphacat.chinastockdata.model.marketindex.MarketIndex;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketService {
  private final EastMoneyMarketIndexService eastMoneyMarketIndexService;

  public MarketService(final EastMoneyMarketIndexService eastMoneyMarketIndexService) {
    this.eastMoneyMarketIndexService = eastMoneyMarketIndexService;
  }

  public List<MarketIndex> getMarketIndex(String indexCode, LocalDate startDate, KLineTypeEnum kType) {
    return eastMoneyMarketIndexService.getMarketIndex(indexCode, startDate, kType);
  }
}
