package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.model.stock.StockLimitDownSummary;
import cn.alphacat.chinastockdata.model.stock.StockLimitUpSummary;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.RequestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EastMoneyMarketStockLimitService {
  private static final String LIMIT_UP_URL = "https://push2ex.eastmoney.com/getTopicZTPool";
  private static final String LIMIT_DOWN_URL = "https://push2ex.eastmoney.com/getTopicDTPool";

  private static final DateTimeFormatter BASE_DATE_TIME_FORMATTER =
      DateTimeFormatter.BASIC_ISO_DATE;
  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public StockLimitDownSummary getStockLimitDownSummary(LocalDate date) {
    String fullUrl = buildLimitDownRequestUrl(date);

    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).GET().build();

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
    JsonNode dataJsonNode = responseJsonNode.get("data");
    if (dataJsonNode == null) {
      return StockLimitDownSummary.builder().limitDownCount(0).build();
    }
    Integer limitUpCount = JsonUtil.safeGetInteger(dataJsonNode, "tc");
    return StockLimitDownSummary.builder().limitDownCount(limitUpCount).build();
  }

  public StockLimitUpSummary getStockLimitUpSummary(LocalDate date) {
    String fullUrl = buildLimitUpRequestUrl(date);

    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).GET().build();

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
    JsonNode dataJsonNode = responseJsonNode.get("data");
    if (dataJsonNode == null) {
      return StockLimitUpSummary.builder().limitUpCount(0).build();
    }
    Integer limitUpCount = JsonUtil.safeGetInteger(dataJsonNode, "tc");
    return StockLimitUpSummary.builder().limitUpCount(limitUpCount).build();
  }

  private static String buildLimitDownRequestUrl(LocalDate date) {
    Map<String, String> requestMap = buildLimitDownParameterMap(date);
    return RequestUtil.buildUrlByParametersMap(LIMIT_DOWN_URL, requestMap);
  }

  private static String buildLimitUpRequestUrl(LocalDate date) {
    Map<String, String> requestMap = buildLimitUpParameterMap(date);
    return RequestUtil.buildUrlByParametersMap(LIMIT_UP_URL, requestMap);
  }

  private static Map<String, String> buildLimitDownParameterMap(LocalDate date) {
    String formatDate = date.format(BASE_DATE_TIME_FORMATTER);

    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
    parameterMap.put("dpt", "wz.ztzt");
    parameterMap.put("Pageindex", "0");
    parameterMap.put("pagesize", "10000");
    parameterMap.put("sort", "fund:asc");

    parameterMap.put("date", formatDate);
    return parameterMap;
  }

  private static Map<String, String> buildLimitUpParameterMap(LocalDate date) {
    String formatDate = date.format(BASE_DATE_TIME_FORMATTER);

    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
    parameterMap.put("dpt", "wz.ztzt");
    parameterMap.put("Pageindex", "0");
    parameterMap.put("pagesize", "10000");
    parameterMap.put("sort", "fbt:asc");

    parameterMap.put("date", formatDate);
    return parameterMap;
  }
}
