package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.LuguLuguIndexPEEnums;
import cn.alphacat.chinastockdata.model.IndexPE;
import cn.alphacat.chinastockdata.model.legulegu.LeguLeguIndexPECsrfResponse;
import cn.alphacat.chinastockdata.util.JsonUtil;
import cn.alphacat.chinastockdata.util.LocalDateUtil;
import com.fasterxml.jackson.databind.JsonNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeguLeguService {
  private final ResourceLoader resourceLoader;

  private static final String USER_AGENT =
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
          + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";

  private static final String API_URL = "https://legulegu.com/api/stockdata/index-basic-pe";

  private static final HttpClient httpClient =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public LeguLeguService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public Map<LocalDate, IndexPE> getStockIndexPE(LuguLuguIndexPEEnums index) {
    String token;
    try {
      token = getToken();
    } catch (Exception e) {
      return null;
    }

    String fullUrl = String.format("%s?token=%s&indexCode=%s", API_URL, token, index.getIndeCode());

    LeguLeguIndexPECsrfResponse leguLeguIndexPECsrfResponse = null;
    try {
      leguLeguIndexPECsrfResponse = fetchCsrfToken();
    } catch (Exception e) {
      return null;
    }
    Map<String, List<String>> headers = leguLeguIndexPECsrfResponse.getCookies();
    List<String> setCookies = headers.getOrDefault("Set-Cookie", Collections.emptyList());
    String cookieHeaderValue =
        setCookies.stream()
            .map(
                cookie -> {
                  int semiColonIndex = cookie.indexOf(';');
                  return (semiColonIndex > 0) ? cookie.substring(0, semiColonIndex) : cookie;
                })
            .collect(Collectors.joining("; "));

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(fullUrl))
            .header("X-CSRF-Token", leguLeguIndexPECsrfResponse.getCsrfToken())
            .header("Cookie", cookieHeaderValue)
            .GET()
            .build();

    HttpResponse<String> response;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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

    JsonNode dataNode = responseJsonNode.get("data");

    Map<LocalDate, IndexPE> result = new LinkedHashMap<>();

    if (!dataNode.isArray()) {
      return null;
    }
    for (JsonNode item : dataNode) {
      Long timestamp = JsonUtil.safeGetLong(item, "date");
      if (timestamp == null) {
        continue;
      }
      LocalDate date = LocalDateUtil.parseTimestamp(timestamp);

      BigDecimal close = JsonUtil.safeGetBigDecimal(item, "close");
      BigDecimal lyrPe = JsonUtil.safeGetBigDecimal(item, "lyrPe");
      BigDecimal addLyrPe = JsonUtil.safeGetBigDecimal(item, "addLyrPe");
      BigDecimal middleLyrPe = JsonUtil.safeGetBigDecimal(item, "middleLyrPe");
      BigDecimal ttmPe = JsonUtil.safeGetBigDecimal(item, "ttmPe");
      BigDecimal addTtmPe = JsonUtil.safeGetBigDecimal(item, "addTtmPe");
      BigDecimal middleTtmPe = JsonUtil.safeGetBigDecimal(item, "middleTtmPe");

      IndexPE row =
          IndexPE.builder()
              .indexCode(index.getIndeCode())
              .date(date)
              .close(close)
              .lyrPe(lyrPe)
              .addLyrPe(addLyrPe)
              .middleLyrPe(middleLyrPe)
              .ttmPe(ttmPe)
              .addTtmPe(addTtmPe)
              .middleTtmPe(middleTtmPe)
              .build();

      result.put(date, row);
    }
    return result;
  }

  private static LeguLeguIndexPECsrfResponse fetchCsrfToken() throws Exception {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://legulegu.com/stockdata/sz50-ttm-lyr"))
            .header("User-Agent", USER_AGENT)
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    Document doc = Jsoup.parse(response.body());
    Element csrfTag = doc.select("meta[name=_csrf]").first();

    if (csrfTag == null) {
      throw new RuntimeException("_csrf meta tag not found in the response.");
    }

    String csrfToken = csrfTag.attr("content");

    LeguLeguIndexPECsrfResponse leguLeguIndexPECsrfResponse =
        LeguLeguIndexPECsrfResponse.builder()
            .cookies(response.headers().map())
            .csrfToken(csrfToken)
            .build();
    return leguLeguIndexPECsrfResponse;
  }

  private String getToken() throws Exception {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("nashorn");
    Resource resource = resourceLoader.getResource("classpath:js/leguleguTokenGeneration.js");
    String jsCode = Files.readString(resource.getFile().toPath());
    engine.eval(jsCode);

    Invocable invocable = (Invocable) engine;
    Object result =
        invocable.invokeFunction("hex", LocalDate.now().format(DateTimeFormatter.ISO_DATE));

    return result.toString().toLowerCase(Locale.ROOT);
  }
}
