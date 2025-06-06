package cn.alphacat.chinastockdata.stock;

import cn.alphacat.chinastockdata.enums.StockExchangeMarketEnums;
import cn.alphacat.chinastockdata.model.stock.StockInfo;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.RequestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BaiduStockInfoHandler {
  private static final String BAIDU_STOCK_INFO_URL =
      "https://finance.pae.baidu.com/selfselect/getmarketrank"
          + "?sort_type=1&sort_key=14&from_mid=1&group=pclist&type=ab&finClientType=pc";
  private static final int MAX_PAGE_SIZE = 20;
  private static final int PAGE_COUNT = 500;

  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public List<StockInfo> getStockInfoList() {
    List<StockInfo> stockInfoList = new ArrayList<>();
    for (int i = 0; i < PAGE_COUNT; i++) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
      int beginNum = i * MAX_PAGE_SIZE;
      String fullUrl = buildUrl(beginNum);
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(fullUrl))
              .header(
                  "User-Agent",
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/110.0")
              .header("Accept", "application/vnd.finance-web.v1+json")
              .header(
                  "Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
              .header("Accept-Encoding", "gzip, deflate, br")
              .header("Content-Type", "application/json")
              .header("Origin", "https://gushitong.baidu.com")
              .header("Referer", "https://gushitong.baidu.com/")
              .header(
                  "Cookie",
                  "BAIDUID=5D6B41AD5BE03619A214B371970EB643:FG=1; BIDUPSID=72A958B07427F9F9CB3F63FD8B6C6565;"
                      + " PSTM=1723618492; ZFY=USOzWykRABpB9kTNtM29hNaXpj:AfNf0O65YLgmVy2Fg:C; "
                      + "H_PS_PSSID=60274_60359_60599_60607_60664_60677_60674_60694_60709; "
                      + "BA_HECTOR=252hagaka4alah2g81a4818036oger1jdasff1u; "
                      + "PSINO=6; delPer=0; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; "
                      + "ab_sr=1.0.1_M2I1MThhZjNiZTMwYzJiZTA1N2RiOTAzZGI4OGZiZTZiOGZiY2RmZTQyY2YxZTlmYWFkZjExODh"
                      + "jZmY1MGM1N2M1YjBlZjhkMzNmZmY3ZjVkYmJmZDE0ODM1MTg5NTQ3MDJkZTFiMGM4MTViMWU2YmYxYjU3ZmVlZGM5"
                      + "NDVhOWIzOWQwMTBmMzBmNTk4NWQ2MmMwYjQ5MDdhNjI2MDY3OA==")
              .GET()
              .build();

      HttpResponse<String> response;
      try {
        response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      } catch (Exception e) {
        break;
      }
      if (response.statusCode() != HttpURLConnection.HTTP_OK) {
        break;
      }
      String body = response.body();
      JsonNode responseJsonNode = JsonUtil.parse(body);
      if (responseJsonNode == null) {
        break;
      }
      JsonNode resultJsonNode = responseJsonNode.path("Result").path("Result");
      if (resultJsonNode.isMissingNode() || !resultJsonNode.isArray()) {
        break;
      }
      JsonNode firstJsonNode = resultJsonNode.get(0);
      if (firstJsonNode == null || firstJsonNode.isMissingNode()) {
        break;
      }
      JsonNode rankJsonNode =
          firstJsonNode
              .path("DisplayData")
              .path("resultData")
              .path("tplData")
              .path("result")
              .path("rank");
      if (rankJsonNode.isMissingNode()) {
        break;
      }
      for (JsonNode node : rankJsonNode) {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setStockCode(JsonUtil.safeGetText(node, "code"));
        stockInfo.setStockName(JsonUtil.safeGetText(node, "name"));
        String exchange = JsonUtil.safeGetText(node, "exchange");
        stockInfo.setExchangeMarket(StockExchangeMarketEnums.getStockExchangeMarketEnums(exchange));
        stockInfoList.add(stockInfo);
      }
    }
    return stockInfoList;
  }

  private String buildUrl(int pageNum) {
    Map<String, String> requestMap = buildParameterMap(pageNum);
    return RequestUtil.buildUrlByParametersMap(BAIDU_STOCK_INFO_URL, requestMap);
  }

  private static HashMap<String, String> buildParameterMap(int beginNum) {
    HashMap<String, String> parameterMap = new HashMap<>();
    parameterMap.put("pn", String.valueOf(beginNum));
    parameterMap.put("rn", String.valueOf(MAX_PAGE_SIZE));
    return parameterMap;
  }
}
