package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.KLineTypeEnum;
import cn.alphacat.chinastockdata.model.marketindex.IndexConstituent;
import cn.alphacat.chinastockdata.model.marketindex.MarketIndex;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketService {
  private final EastMoneyMarketIndexService eastMoneyMarketIndexService;
  private final MarketIndexConstituentHandler marketIndexConstituentHandler;

  public MarketService(
      final EastMoneyMarketIndexService eastMoneyMarketIndexService,
      final MarketIndexConstituentHandler marketIndexConstituentHandler) {
    this.eastMoneyMarketIndexService = eastMoneyMarketIndexService;
    this.marketIndexConstituentHandler = marketIndexConstituentHandler;
  }

  public List<MarketIndex> getMarketIndex(
      String indexCode, LocalDate startDate, KLineTypeEnum kType) {
    return eastMoneyMarketIndexService.getMarketIndex(indexCode, startDate, kType);
  }

  public List<IndexConstituent> getCSI300IndexConstituent() {
    return marketIndexConstituentHandler.getCSI300IndexConstituent();
  }
}
