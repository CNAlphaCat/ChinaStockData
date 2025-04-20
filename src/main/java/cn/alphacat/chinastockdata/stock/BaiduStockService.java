package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.model.StockMin;
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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BaiduStockService {
  private static final String BAIDU_MARKET_MIN_URL =
      "https://finance.pae.baidu.com/selfselect/getstockquotation";

  private static final DateTimeFormatter TRADE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String[] _MARKET_MIN_COLUMNS = {
    "stock_code", "trade_time", "price", "change", "change_pct", "volume", "avg_price", "amount"
  };

  private static final Map<String, String> JSON_HEADERS = new HashMap<>();

  static {
    JSON_HEADERS.put("Host", "finance.pae.baidu.com");
    JSON_HEADERS.put(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/110.0");
    JSON_HEADERS.put("Accept", "application/vnd.finance-web.v1+json");
    JSON_HEADERS.put(
        "Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
    JSON_HEADERS.put("Accept-Encoding", "gzip, deflate, br");
    JSON_HEADERS.put("Content-Type", "application/json");
    JSON_HEADERS.put("Origin", "https://gushitong.baidu.com");
    JSON_HEADERS.put("Connection", "keep-alive");
    JSON_HEADERS.put("Referer", "https://gushitong.baidu.com/");
    // JSON_HEADERS.put("Cookie", cookie); // 请根据实际情况添加cookie
  }

  public ArrayList<StockMin> getStockMin(String stockCode) {
    if (stockCode == null || stockCode.isEmpty()) {
      return null;
    }

    Map<String, String> params = new HashMap<>();
    params.put("all", "1");
    params.put("isIndex", "false");
    params.put("isBk", "false");
    params.put("isBlock", "false");
    params.put("isFutures", "false");
    params.put("isStock", "true");
    params.put("newFormat", "1");
    params.put("group", "quotation_minute_ab");
    params.put("finClientType", "pc");
    params.put("code", stockCode);

    String response = sendGetRequest(BAIDU_MARKET_MIN_URL, params);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonResponse = objectMapper.readTree(response);
      if (!jsonResponse.has("Result") || jsonResponse.get("Result").isEmpty()) {
        return new ArrayList<>();
      }
      JsonNode result = jsonResponse.get("Result");

      if (!result.has("priceinfo") || result.get("priceinfo").isEmpty()) {
        return new ArrayList<>();
      }
      JsonNode priceInfo = result.get("priceinfo");

      ArrayList<StockMin> resultData = new ArrayList<>();
      for (JsonNode priceNode : priceInfo) {
        StockMin stockMin = buildMarketMin(stockCode, priceNode);
        resultData.add(stockMin);
      }
      return resultData;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private StockMin buildMarketMin(String stockCode, JsonNode priceNode) {
    StockMin stockMin = new StockMin();
    stockMin.setStockCode(stockCode);

    String time = priceNode.get("time").asText();
    String price = priceNode.get("price").asText();
    String ratio = priceNode.get("ratio").asText();
    String increase = priceNode.get("increase").asText();
    String volume = priceNode.get("volume").asText();
    String avgPrice = priceNode.get("avgPrice").asText();
    String amount = priceNode.get("amount").asText();
    String oriAmount = priceNode.get("oriAmount").asText();

    LocalDateTime tradeTime =
        LocalDateTime.ofEpochSecond(Long.parseLong(time), 0, ZoneOffset.UTC)
            .atZone(ZoneId.of("Asia/Shanghai"))
            .toLocalDateTime();
    stockMin.setTradeTime(tradeTime);

    stockMin.setAveragePrice(new BigDecimal(avgPrice));
    stockMin.setClosePrice(new BigDecimal(price));
    stockMin.setVolume(new BigDecimal(volume).multiply(BigDecimal.valueOf(100)));
    stockMin.setAmount(new BigDecimal(oriAmount).multiply(BigDecimal.valueOf(10000)));

    BigDecimal change = new BigDecimal(increase.replace("+", ""));
    stockMin.setChange(change);

    BigDecimal changePct =
        new BigDecimal(ratio.replace("+", "").replace("%", ""))
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    stockMin.setChangePercent(changePct);

    return stockMin;
  }

  private String sendGetRequest(String urlStr, Map<String, String> params) {
    StringBuilder result = new StringBuilder();
    try {
      URL url = new URL(urlStr + "?" + getParamsString(params));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      // 设置请求头
      for (Map.Entry<String, String> entry : JSON_HEADERS.entrySet()) {
        conn.setRequestProperty(entry.getKey(), entry.getValue());
      }

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
