package cn.alphacat.chinastockdata.bond;

import cn.alphacat.chinastockdata.model.bond.TreasuryBond;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.RequestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TreasuryBondService {
  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .cookieHandler(new java.net.CookieManager())
          .build();

  private static final String EAST_MONEY_URL = "https://datacenter.eastmoney.com/api/data/get";

  public LinkedHashMap<LocalDate, TreasuryBond> getTreasuryBondDataMap(LocalDate startDate) {
    JsonNode firstPageJsonNode = getTreasureBondDataResultJsonNode(1);

    if (firstPageJsonNode == null) {
      return null;
    }
    Map<LocalDate, TreasuryBond> localDateTenYearTreasuryBondMap =
        getDescLocalDateTenYearTreasuryBondMap(firstPageJsonNode);
    LinkedHashMap<LocalDate, TreasuryBond> result =
        new LinkedHashMap<>(localDateTenYearTreasuryBondMap);

    LocalDate startDateInResult = localDateTenYearTreasuryBondMap.keySet().iterator().next();
    if (startDateInResult.isBefore(startDate)) {
      LinkedHashMap<LocalDate, TreasuryBond> filteredResult =
          result.entrySet().stream()
              .filter(entry -> !entry.getKey().isBefore(startDate))
              .sorted(Map.Entry.<LocalDate, TreasuryBond>comparingByKey().reversed())
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (oldValue, newValue) -> oldValue,
                      LinkedHashMap::new));
      return filteredResult;
    }

    Long pages = JsonUtil.safeGetLong(firstPageJsonNode, "pages");
    if (pages == null) {
      return null;
    }

    for (int i = 2; i <= pages; i++) {
      JsonNode currentPageJsonNode = getTreasureBondDataResultJsonNode(i);
      if (currentPageJsonNode == null) {
        break;
      }
      LinkedHashMap<LocalDate, TreasuryBond> currentPageJsonNodeMap =
          getDescLocalDateTenYearTreasuryBondMap(currentPageJsonNode);
      result.putAll(currentPageJsonNodeMap);
      LocalDate currentStartDateInResult = currentPageJsonNodeMap.keySet().iterator().next();
      if (currentStartDateInResult.isBefore(startDate)
          || currentStartDateInResult.isEqual(startDate)) {
        break;
      }
    }
    LinkedHashMap<LocalDate, TreasuryBond> filteredResult =
        result.entrySet().stream()
            .filter(entry -> !entry.getKey().isBefore(startDate))
            .sorted(Map.Entry.<LocalDate, TreasuryBond>comparingByKey().reversed())
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue,
                    LinkedHashMap::new));
    return filteredResult;
  }

  private static LinkedHashMap<LocalDate, TreasuryBond> getDescLocalDateTenYearTreasuryBondMap(
      JsonNode resultNode) {
    if (!resultNode.has("data")) {
      return new LinkedHashMap<>();
    }
    JsonNode dataNode = resultNode.get("data");

    return StreamSupport.stream(dataNode.spliterator(), false)
        .map(
            data -> {
              LocalDate solarDate = JsonUtil.safeGetLocalDate(data, "SOLAR_DATE");
              if (solarDate == null) {
                return null;
              }
              BigDecimal twoYearTreasuryBondField = JsonUtil.safeGetBigDecimal(data, "EMM00588704");
              BigDecimal fiveYearTreasuryBondField =
                  JsonUtil.safeGetBigDecimal(data, "EMM00166462");
              BigDecimal tenYearTreasuryBondField = JsonUtil.safeGetBigDecimal(data, "EMM00166466");
              BigDecimal thirtyYearTreasuryBondField =
                  JsonUtil.safeGetBigDecimal(data, "EMM00166469");

              BigDecimal twoYearMinusTenYearTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMM01276014");

              BigDecimal twoYearUSTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMG00001306");
              BigDecimal fiveYearUSTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMG00001308");
              BigDecimal tenYearUSTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMG00001310");
              BigDecimal thirtyYearUSTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMG00001312");

              BigDecimal twoYearMinusTenYearUSTreasuryBondYield =
                  JsonUtil.safeGetBigDecimal(data, "EMG01339436");

              BigDecimal chinaGDPGrowthRate = JsonUtil.safeGetBigDecimal(data, "EMM00000024");
              BigDecimal usGDPGrowthRate = JsonUtil.safeGetBigDecimal(data, "EMG00159635");

              return new AbstractMap.SimpleEntry<>(
                  solarDate,
                  TreasuryBond.builder()
                      .twoYearTreasuryBondYield(twoYearTreasuryBondField)
                      .fiveYearTreasuryBondYield(fiveYearTreasuryBondField)
                      .tenYearTreasuryBondYield(tenYearTreasuryBondField)
                      .thirtyYearTreasuryBondYield(thirtyYearTreasuryBondField)
                      .twoYearMinusTenYearTreasuryBondYield(twoYearMinusTenYearTreasuryBondYield)
                      .twoYearUSTreasuryBondYield(twoYearUSTreasuryBondYield)
                      .fiveYearUSTreasuryBondYield(fiveYearUSTreasuryBondYield)
                      .tenYearUSTreasuryBondYield(tenYearUSTreasuryBondYield)
                      .thirtyYearUSTreasuryBondYield(thirtyYearUSTreasuryBondYield)
                      .twoYearMinusTenYearUSTreasuryBondYield(
                          twoYearMinusTenYearUSTreasuryBondYield)
                      .chinaGDPGrowthRate(chinaGDPGrowthRate)
                      .usGDPGrowthRate(usGDPGrowthRate)
                      .solarDate(solarDate)
                      .build());
            })
        .filter(Objects::nonNull)
        .sorted(Map.Entry.comparingByKey())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> newVal,
                LinkedHashMap::new));
  }

  private static JsonNode getTreasureBondDataResultJsonNode(int page) {
    String url = buildRequestUrl(page);
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
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
    return responseJsonNode.get("result");
  }

  private static String buildRequestUrl(int page) {
    Map<String, String> requestMap = getTreasuryBondRequestMap(page);
    return RequestUtil.buildUrlByParametersMap(EAST_MONEY_URL, requestMap);
  }

  private static Map<String, String> getTreasuryBondRequestMap(int page) {
    String pageStr = String.valueOf(page);
    HashMap<String, String> map = new HashMap<>();
    map.put("type", "RPTA_WEB_TREASURYYIELD");
    map.put("sty", "ALL");
    map.put("st", "SOLAR_DATE");
    map.put("sr", "-1");
    map.put("token", "894050c76af8597a853f5b408b759f5d");
    map.put("ps", "500");
    map.put("p", pageStr);
    map.put("pageNo", pageStr);
    map.put("pageNum", pageStr);
    return map;
  }
}
