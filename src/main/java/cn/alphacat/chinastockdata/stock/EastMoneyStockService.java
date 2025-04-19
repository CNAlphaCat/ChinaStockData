package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.MarketMin;
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
import java.util.List;
import java.util.Map;

@Service
public class EastMoneyStockService {
  private static final String EASTMONEY_MARKET_MIN_URL =
      "https://push2.eastmoney.com/api/qt/stock/trends2/get";

  private static final DateTimeFormatter TRADE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public List<MarketMin> buildMarketMin(String stockCode) {
    if (stockCode == null || stockCode.isEmpty()) {
      stockCode = "000001";
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

      List<MarketMin> result = new ArrayList<>();
      for (JsonNode trendNode : trends) {
        String[] trendData = trendNode.asText().split(",");
        MarketMin marketMin = buildMarketMin(stockCode, trendData, preClose);
        result.add(marketMin);
      }
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  private static MarketMin buildMarketMin(
      String stockCode, String[] trendData, BigDecimal preClose) {
    MarketMin marketMin = new MarketMin();
    marketMin.setStockCode(stockCode);
    String tradeTime = trendData[0];
    marketMin.setTradeTime(LocalDateTime.parse(tradeTime, TRADE_TIME_FORMATTER));
    BigDecimal price = new BigDecimal(trendData[1]);
    marketMin.setPrice(price);

    BigDecimal change = price.subtract(preClose);
    marketMin.setChange(change);

    BigDecimal changePct =
        change
            .divide(preClose, 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    marketMin.setChangePercent(changePct);
    marketMin.setAveragePrice(price);
    BigDecimal volume = new BigDecimal(trendData[5]).multiply(BigDecimal.valueOf(100));
    marketMin.setVolume(volume);

    BigDecimal amount = new BigDecimal(trendData[6]);
    marketMin.setAmount(amount);
    return marketMin;
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
