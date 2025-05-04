package cn.alphacat.chinastockdata.future;

import cn.alphacat.chinastockdata.model.future.FutureDetail;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FutureDetailHandler {

  public List<FutureDetail> getFutureDetails(String secId, int maxCount) {
    String url = "https://push2.eastmoney.com/api/qt/stock/details/get";
    url = url + "?" + "secid=" + secId;
    url = url + "&" + "fields1=f1,f2,f3,f4,f5";
    url = url + "&" + "fields2=f51,f52,f53,f54,f55";
    url = url + "&" + "pos=" + (-maxCount);

    HttpResponse<String> response;
    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    String body = response.body();
    JsonNode jsonNode = JsonUtil.parse(body);

    JsonNode dataNode = jsonNode.path("data");
    JsonNode detailsNode = dataNode.path("details");
    String prePrice = dataNode.path("prePrice").asText();

    List<FutureDetail> futureDetails = new ArrayList<>();
    Iterator<JsonNode> elements = detailsNode.elements();
    while (elements.hasNext()) {
      JsonNode detailNode = elements.next();
      String detailText = detailNode.asText();

      String[] parts = detailText.split(",");

      if (parts.length >= 4) {
        String time = parts[0];
        String price = parts[1];
        String volume = parts[2];
        String tradeCount = parts[3];

        FutureDetail futureDetail = new FutureDetail();
        futureDetail.setTradeTime(LocalDateTimeUtil.parseTodayTimeOfPatternHH_mm_ss(time));
        futureDetail.setPrice(new BigDecimal(price));
        futureDetail.setVolume(new BigDecimal(volume));
        futureDetail.setTradeCount(new BigDecimal(tradeCount));

        futureDetails.add(futureDetail);
      }
    }

    return futureDetails;
  }
}
