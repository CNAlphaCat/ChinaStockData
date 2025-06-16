package cn.alphacat.chinastockdata.future.handler;

import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineTypeEnum;
import cn.alphacat.chinastockdata.enums.EastMoneyQTKlineWeightingEnum;
import cn.alphacat.chinastockdata.enums.FutureHistoryEnum;
import cn.alphacat.chinastockdata.model.future.FutureHistory;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.LocalDateTimeUtil;
import cn.alphacat.chinastockdata.util.LocalDateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EastMoneyFutureHistoryHandler {
  private static final String EASTMONEY_KLINE_FIELDS_URL =
      "https://push2his.eastmoney.com/api/qt/stock/kline/get";
  private static final String FIELDS1 = "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13";
  private static final String RTNTYPE = "6";

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  public List<FutureHistory> getFutureHistory(
      String code,
      LocalDate beg,
      LocalDate end,
      EastMoneyQTKlineTypeEnum klt,
      EastMoneyQTKlineWeightingEnum fqt) {
    String fields2 =
        Arrays.stream(FutureHistoryEnum.values())
            .map(FutureHistoryEnum::getKey)
            .collect(Collectors.joining(","));

    String begStr = beg.format(DATE_TIME_FORMATTER);
    String endStr = end.format(DATE_TIME_FORMATTER);

    String url =
        EASTMONEY_KLINE_FIELDS_URL
            + "?"
            + "fields1="
            + FIELDS1
            + "&fields2="
            + fields2
            + "&beg="
            + begStr
            + "&end="
            + endStr
            + "&rtntype="
            + RTNTYPE
            + "&secid="
            + code
            + "&klt="
            + klt.getKey()
            + "&fqt="
            + fqt.getKey();

    HttpResponse<String> response;
    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .headers(
                  "User-Agent",
                  "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko",
                  "Accept",
                  "*/*",
                  "Accept-Language",
                  "zh-CN,zh;q=0.8,zh;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
              .GET()
              .build();

      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      return null;
    }
    return parseResponse(response.body());
  }

  private List<FutureHistory> parseResponse(String response) {
    try {
      JsonNode rootNode = JsonUtil.parse(response);
      JsonNode klinesNode = rootNode.path("data").path("klines");
      if (!klinesNode.isArray() || klinesNode.isEmpty()) {
        return new ArrayList<>();
      }

      List<FutureHistory> futureHistories = new ArrayList<>();
      Iterator<JsonNode> elements = klinesNode.elements();
      while (elements.hasNext()) {
        JsonNode klineNode = elements.next();
        FutureHistory futureHistory = getFutureHistory(klineNode);
        futureHistories.add(futureHistory);
      }

      return futureHistories;
    } catch (Exception e) {
      return null;
    }
  }

  private static FutureHistory getFutureHistory(JsonNode klineNode) {
    String[] klineData = klineNode.asText().split(",");
    FutureHistory futureHistory = new FutureHistory();
    futureHistory.setDate(LocalDateUtil.autoParseDate(klineData[0]));
    futureHistory.setDateTime(LocalDateTimeUtil.autoParseDateTime(klineData[0]));
    futureHistory.setOpen(new BigDecimal(klineData[1]));
    futureHistory.setClose(new BigDecimal(klineData[2]));
    futureHistory.setHigh(new BigDecimal(klineData[3]));
    futureHistory.setLow(new BigDecimal(klineData[4]));
    futureHistory.setVolume(new BigDecimal(klineData[5]));
    futureHistory.setAmount(new BigDecimal(klineData[6]));
    futureHistory.setAmplitude(new BigDecimal(klineData[7]));
    futureHistory.setChangeRate(new BigDecimal(klineData[8]));
    futureHistory.setChangeAmount(new BigDecimal(klineData[9]));
    futureHistory.setTurnoverRate(new BigDecimal(klineData[10]));
    return futureHistory;
  }
}
