package cn.alphacat.chinastockdata.model.future;

import cn.alphacat.chinastockdata.enums.EastMoneyQuoteFieldEnum;
import cn.alphacat.chinastockdata.enums.FutureMarketCodeEnum;
import cn.alphacat.chinastockdata.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FutureMarketOverview {
  private String featureCode;
  private String featureName;
  private BigDecimal changePercent;
  private BigDecimal price;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal openPrice;
  private BigDecimal changePrice;
  private String exchangeRate;
  private BigDecimal volumeRatio;
  private String dynamicPE;
  private BigDecimal volume;
  private BigDecimal amount;
  private BigDecimal closePriceYesterday;
  private BigDecimal totalMarketValue;
  private String currentMarketValue;
  private String marketCode;
  private String marketName;
  private LocalDateTime updateTime;
  private LocalDate latestTradeDate;

  public FutureMarketOverview(JsonNode diffNode) {
    this.featureCode = JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.CODE.getKey());
    this.featureName = JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.NAME.getKey());
    this.changePercent =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.CHANGE_RATE.getKey());
    this.price =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.LATEST_PRICE.getKey());
    this.highPrice = JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.HIGHEST.getKey());
    this.lowPrice = JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.LOWEST.getKey());
    this.openPrice =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.OPEN_PRICE.getKey());
    this.changePrice =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.CHANGE_AMOUNT.getKey());
    this.exchangeRate =
        JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.TURN_OVER_RATE.getKey());
    this.volumeRatio =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.VOLUME_RATIO.getKey());
    this.dynamicPE = JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.DYNAMIC_PE.getKey());
    this.volume = JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.VOLUME.getKey());
    this.amount = JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.TURN_OVER.getKey());
    this.closePriceYesterday =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.PREVIOUS_CLOSE.getKey());
    this.totalMarketValue =
        JsonUtil.safeGetBigDecimal(diffNode, EastMoneyQuoteFieldEnum.TOTAL_MARKET_CAP.getKey());
    this.currentMarketValue =
        JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.CIRCULATING_MARKET_CAP.getKey());
    this.marketCode = JsonUtil.safeGetText(diffNode, EastMoneyQuoteFieldEnum.MARKET_CODE.getKey());
    this.marketName = FutureMarketCodeEnum.getDescriptionByCode(this.marketCode);
    this.updateTime =
        JsonUtil.safeGetLocalDateTime(diffNode, EastMoneyQuoteFieldEnum.UPDATE_TIMESTAMP.getKey());
    this.latestTradeDate =
        JsonUtil.safeGetLocalDateOfPatternyyyyMMdd(
            diffNode, EastMoneyQuoteFieldEnum.LATEST_TRADE_DATE.getKey());
  }
}
