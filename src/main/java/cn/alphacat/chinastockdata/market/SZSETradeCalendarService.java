package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.TradeStatusEnum;
import cn.alphacat.chinastockdata.model.SZSECalendar;
import cn.alphacat.chinastockdata.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class SZSETradeCalendarService {
  private static final String CALENDAR_URL =
      "http://www.szse.cn/api/report/exchange/onepersistenthour/monthList?month=";
  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public List<SZSECalendar> getTradeCalendar(Integer year) {
    List<SZSECalendar> tradeCalendar = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      List<SZSECalendar> currentMonth = getTradeCalendar(year, i + 1);
      tradeCalendar.addAll(currentMonth);
    }
    return tradeCalendar;
  }

  public List<SZSECalendar> getTradeCalendar(Integer year, Integer month) {
    String url = CALENDAR_URL + year + "-" + month;
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
    JsonNode data = responseJsonNode.get("data");
    ArrayList<SZSECalendar> szseCalendars = new ArrayList<>();
    for (JsonNode item : data) {
      Integer tradeStatusNum = JsonUtil.safeGetInteger(item, "jybz");
      TradeStatusEnum tradeStatusEnum = TradeStatusEnum.fromStatus(tradeStatusNum);

      SZSECalendar szseCalendar = new SZSECalendar();
      szseCalendar.setTradeDate(JsonUtil.safeGetLocalDate(item, "jyrq"));
      szseCalendar.setDayWeek(JsonUtil.safeGetInteger(item, "zrxh"));
      szseCalendar.setTradeStatus(tradeStatusEnum);
      szseCalendars.add(szseCalendar);
    }
    return szseCalendars;
  }
}
