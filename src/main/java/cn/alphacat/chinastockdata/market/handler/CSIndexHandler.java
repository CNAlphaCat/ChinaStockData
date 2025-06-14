package cn.alphacat.chinastockdata.market.handler;

import cn.alphacat.chinastockdata.model.marketindex.IndexPE;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.RequestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class CSIndexHandler {
  private static final String API_URL = "https://www.csindex.com.cn/csindex-home/perf/index-perf";

  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public Map<LocalDate, IndexPE> getStockIndexPE(
      String indexCode, LocalDate startDate, LocalDate endDate) {
    Map<String, String> requestMap = buildParams(indexCode, startDate, endDate);
    String url = RequestUtil.buildUrlByParametersMap(API_URL, requestMap);

    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

    HttpResponse<String> response;
    try {
      response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      log.error("getStockIndexPE error {}", e.getMessage());
      return new HashMap<>();
    }
    if (response.statusCode() != HttpURLConnection.HTTP_OK) {
      log.error("getStockIndexPE error {}", response.statusCode());
      return new HashMap<>();
    }
    String body = response.body();
    JsonNode responseJsonNode = JsonUtil.parse(body);

    if (responseJsonNode == null) {
      log.error("getStockIndexPE error Null response");
      return new HashMap<>();
    }

    JsonNode dataNode = responseJsonNode.get("data");

    Map<LocalDate, IndexPE> result = new LinkedHashMap<>();

    if (!dataNode.isArray()) {
      log.error("getStockIndexPE error Null dataNode");
      return new HashMap<>();
    }

    for (JsonNode item : dataNode) {
      LocalDate tradeDate = JsonUtil.safeGetLocalDate(item, "tradeDate");
      if (tradeDate == null) {
        continue;
      }
      BigDecimal addTtmPe = JsonUtil.safeGetBigDecimal(item, "peg");
      IndexPE row =
          IndexPE.builder().indexCode(indexCode).date(tradeDate).addTtmPe(addTtmPe).build();
      result.put(tradeDate, row);
    }
    return result;
  }

  private HashMap<String, String> buildParams(
      String indexCode, LocalDate startDate, LocalDate endDate) {
    HashMap<String, String> params = new HashMap<>();
    params.put("indexCode", indexCode);
    params.put("startDate", startDate.format(DateTimeFormatter.BASIC_ISO_DATE));
    params.put("endDate", endDate.format(DateTimeFormatter.BASIC_ISO_DATE));
    return params;
  }
}
