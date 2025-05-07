package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.model.stock.StockKline;
import cn.alphacat.chinastockdata.model.stock.StockKlineData;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.LocalDateTimeUtil;
import cn.alphacat.chinastockdata.util.LocalDateUtil;
import cn.alphacat.chinastockdata.util.RequestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EastMoneyStockKlineHandler {
  private static final String STOCK_KLINE_URL =
      "http://push2his.eastmoney.com/api/qt/stock/kline/get";

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
  private static final EastMoneyQTKlineWeightingEnum DEFAULT_WEIGHTING =
      EastMoneyQTKlineWeightingEnum.PRE_WEIGHTING;

  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public StockKlineData getStockKlineData(
      String stockCode, EastMoneyQTKlineTypeEnum kLineTypeEnum, LocalDate startDate) {
    return getStockKlineData(
        stockCode, kLineTypeEnum, startDate, LocalDateUtil.getNow(), DEFAULT_WEIGHTING);
  }

  public StockKlineData getStockKlineData(
      String stockCode,
      EastMoneyQTKlineTypeEnum kLineTypeEnum,
      LocalDate startDate,
      LocalDate endDate) {
    return getStockKlineData(stockCode, kLineTypeEnum, startDate, endDate, DEFAULT_WEIGHTING);
  }

  public StockKlineData getStockKlineData(
      String stockCode,
      EastMoneyQTKlineTypeEnum kLineTypeEnum,
      LocalDate startDate,
      LocalDate endDate,
      EastMoneyQTKlineWeightingEnum eastMoneyQTKlineWeightingEnum) {
    JsonNode dataJsonNode =
        getStockKline(stockCode, kLineTypeEnum, eastMoneyQTKlineWeightingEnum, startDate, endDate);
    if (dataJsonNode == null) {
      return null;
    }
    String stockName = JsonUtil.safeGetText(dataJsonNode, "name");
    Integer decimal = JsonUtil.safeGetInteger(dataJsonNode, "decimal");
    BigDecimal preKPrice = JsonUtil.safeGetBigDecimal(dataJsonNode, "preKPrice");
    JsonNode kLinesJsonNode = dataJsonNode.get("klines");

    List<StockKline> stockKlines = new ArrayList<>();

    if(kLinesJsonNode == null){
      return null;
    }

    for (JsonNode kLineJsonNode : kLinesJsonNode) {
      String klineString = kLineJsonNode.asText();
      String[] klineArray = klineString.split(",");
      LocalDate date = LocalDateUtil.autoParseDate(klineArray[0]);
      LocalDateTime dateTime = LocalDateTimeUtil.autoParseDateTime(klineArray[0]);
      BigDecimal open = new BigDecimal(klineArray[1]);
      BigDecimal close = new BigDecimal(klineArray[2]);
      BigDecimal high = new BigDecimal(klineArray[3]);
      BigDecimal low = new BigDecimal(klineArray[4]);
      BigDecimal volume = new BigDecimal(klineArray[5]);
      BigDecimal amount = new BigDecimal(klineArray[6]);
      BigDecimal changePercent = new BigDecimal(klineArray[8]);
      BigDecimal change = new BigDecimal(klineArray[9]);
      BigDecimal turnoverRatio = new BigDecimal(klineArray[10]);

      StockKline stockKline =
          StockKline.builder()
              .date(date)
              .dateTime(dateTime)
              .open(open)
              .close(close)
              .high(high)
              .low(low)
              .volume(volume)
              .amount(amount)
              .changePercent(changePercent)
              .change(change)
              .turnoverRatio(turnoverRatio)
              .build();
      stockKlines.add(stockKline);
    }

    return StockKlineData.builder()
        .stockCode(stockCode)
        .stockName(stockName)
        .decimal(decimal)
        .preKPrice(preKPrice)
        .kLines(stockKlines)
        .build();
  }

  private JsonNode getStockKline(
      String stockCode,
      EastMoneyQTKlineTypeEnum kLineTypeEnum,
      EastMoneyQTKlineWeightingEnum eastMoneyQTKlineWeightingEnum,
      LocalDate startDate,
      LocalDate endDate) {
    Map<String, String> requestMap =
        buildParametersMap(
            stockCode, kLineTypeEnum, eastMoneyQTKlineWeightingEnum, startDate, endDate);
    String url = RequestUtil.buildUrlByParametersMap(STOCK_KLINE_URL, requestMap);

    URI uri = URI.create(url);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

    HttpResponse<String> response;
    try {
      response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      return null;
    }
    if (response.statusCode() != HttpURLConnection.HTTP_OK) {
      return null;
    }
    String body = response.body();
    JsonNode responseJsonNode = JsonUtil.parse(body);
    if (responseJsonNode == null) {
      return null;
    }
    return responseJsonNode.get("data");
  }

  private static Map<String, String> buildParametersMap(
      String stockCode,
      EastMoneyQTKlineTypeEnum kLineTypeEnum,
      EastMoneyQTKlineWeightingEnum eastMoneyQTKlineWeightingEnum,
      LocalDate startDate,
      LocalDate endDate) {
    Map<String, String> parametersMap = new HashMap<>();
    parametersMap.put("fields1", "f1,f2,f3,f4,f5,f6");
    parametersMap.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61,f116");
    parametersMap.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
    parametersMap.put("klt", kLineTypeEnum.getKey());
    parametersMap.put("fqt", eastMoneyQTKlineWeightingEnum.getKey());
    parametersMap.put("secid", getSecid(stockCode));
    parametersMap.put("beg", startDate.format(DATE_FORMATTER));
    parametersMap.put("end", endDate.format(DATE_FORMATTER));
    parametersMap.put("_", "1623766962675");
    return parametersMap;
  }

  private static String getSecid(String stockCode) {
    if (stockCode.startsWith("6")) {
      return "1." + stockCode;
    }
    return "0." + stockCode;
  }
}
