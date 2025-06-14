package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.LuguLuguIndexPEEnums;
import cn.alphacat.chinastockdata.market.handler.CSIndexHandler;
import cn.alphacat.chinastockdata.market.handler.LeguLeguHandler;
import cn.alphacat.chinastockdata.model.marketindex.IndexPE;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class MarketIndexService {
  private final LeguLeguHandler leguLeguHandler;
  private final CSIndexHandler csIndexHandler;

  public MarketIndexService(
      final LeguLeguHandler leguLeguHandler, final CSIndexHandler csIndexHandler) {
    this.leguLeguHandler = leguLeguHandler;
    this.csIndexHandler = csIndexHandler;
  }

  public Map<LocalDate, IndexPE> getCSI300PE(LocalDate startDate) {
    Map<LocalDate, IndexPE> stockIndexPE =
        csIndexHandler.getStockIndexPE("000300", startDate, LocalDate.now());
    if (stockIndexPE.isEmpty()) {
      stockIndexPE = leguLeguHandler.getStockIndexPE(LuguLuguIndexPEEnums.SCI300);
    }
    return stockIndexPE;
  }
}
