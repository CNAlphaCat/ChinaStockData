package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.KLineType;
import cn.alphacat.chinastockdata.model.MarketIndex;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketService {
  private final EastMoneyMarketIndexService eastMoneyMarketIndexService;

  public MarketService(final EastMoneyMarketIndexService eastMoneyMarketIndexService) {
    this.eastMoneyMarketIndexService = eastMoneyMarketIndexService;
  }

  public List<MarketIndex> getMarketIndex(String indexCode, LocalDate startDate, KLineType kType) {
    return eastMoneyMarketIndexService.getMarketIndex(indexCode, startDate, kType);
  }
}
