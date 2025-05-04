package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.stock.StockMin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EastMoneyStockService {
  private static final String EASTMONEY_MARKET_MIN_URL =
      "https://push2.eastmoney.com/api/qt/stock/trends2/get";

  private static final DateTimeFormatter TRADE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private static final int TRADE_TIME_INDEX = 0;
  private static final int OPEN_PRICE_INDEX = 1;
  private static final int CLOSE_PRICE_INDEX = 2;
  private static final int HIGH_PRICE_INDEX = 3;
  private static final int LOW_PRICE_INDEX = 4;
  private static final int VOLUME_INDEX = 5;
  private static final int AMOUNT_INDEX = 6;
  private static final int AVERAGE_PRICE_INDEX = 7;

  public ArrayList<StockMin> getStockMin(String stockCode) {
    if (stockCode == null || stockCode.isEmpty()) {
      return null;
    }

    int seCid = stockCode.startsWith("6") ? 1 : 0;
    Map<String, String> params = new HashMap<>();
    params.put("fields1", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13");
    params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58");
    params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
    params.put("ndays", "1");
    params.put("iscr", "1");
    params.put("iscca", "0");
    params.put("secid", seCid + "." + stockCode);
    params.put("_", String.valueOf(System.currentTimeMillis()));

    String response = sendGetRequest(EASTMONEY_MARKET_MIN_URL, params);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonResponse = objectMapper.readTree(response);
      if (!jsonResponse.has("data") || jsonResponse.get("data").isEmpty()) {
        return new ArrayList<>();
      }
      JsonNode data = jsonResponse.get("data");

      BigDecimal preClose = BigDecimal.valueOf(data.get("preClose").asDouble());
      JsonNode trends = data.get("trends");

      ArrayList<StockMin> result = new ArrayList<>();
      for (JsonNode trendNode : trends) {
        String[] trendData = trendNode.asText().split(",");
        StockMin stockMin = getStockMin(stockCode, trendData, preClose);
        result.add(stockMin);
      }
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  private static StockMin getStockMin(
      String stockCode, String[] trendData, BigDecimal preClose) {
    StockMin stockMin = new StockMin();
    stockMin.setStockCode(stockCode);
    String tradeTime = trendData[TRADE_TIME_INDEX];
    String open = trendData[OPEN_PRICE_INDEX];
    String closeString = trendData[CLOSE_PRICE_INDEX];
    String high = trendData[HIGH_PRICE_INDEX];
    String low = trendData[LOW_PRICE_INDEX];
    String volumeString = trendData[VOLUME_INDEX];
    String amountString = trendData[AMOUNT_INDEX];
    String averagePriceString = trendData[AVERAGE_PRICE_INDEX];

    stockMin.setTradeTime(LocalDateTime.parse(tradeTime, TRADE_TIME_FORMATTER));

    stockMin.setAveragePrice(new BigDecimal(averagePriceString));
    stockMin.setOpenPrice(new BigDecimal(open));
    stockMin.setHighPrice(new BigDecimal(high));
    stockMin.setLowPrice(new BigDecimal(low));
    stockMin.setAmount(new BigDecimal(amountString));

    BigDecimal close = new BigDecimal(closeString);
    stockMin.setClosePrice(close);

    BigDecimal change = close.subtract(preClose);
    stockMin.setChange(change);

    BigDecimal changePct =
        change
            .divide(preClose, 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    stockMin.setChangePercent(changePct);

    BigDecimal volume = new BigDecimal(volumeString).multiply(BigDecimal.valueOf(100));
    stockMin.setVolume(volume);

    return stockMin;
  }

  private String sendGetRequest(String urlStr, Map<String, String> params) {
    StringBuilder result = new StringBuilder();
    try {
      URL url = new URL(urlStr + "?" + getParamsString(params));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        result.append(inputLine);
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result.toString();
  }

  private String getParamsString(Map<String, String> params) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (first) {
        first = false;
      } else {
        result.append("&");
      }
      result.append(entry.getKey());
      result.append("=");
      result.append(entry.getValue());
    }
    return result.toString();
  }
}
