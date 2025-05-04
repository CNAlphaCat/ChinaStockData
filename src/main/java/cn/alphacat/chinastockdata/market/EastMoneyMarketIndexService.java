package cn.alphacat.chinastockdata.market;

import cn.alphacat.chinastockdata.enums.KLineTypeEnum;
import cn.alphacat.chinastockdata.model.marketindex.MarketIndex;
import cn.alphacat.chinastockdata.model.marketindex.MarketIndexData;
import cn.alphacat.chinastockdata.model.MarketIndexResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EastMoneyMarketIndexService {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
  private static final DateTimeFormatter DATE_FORMATTER_DASH =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATETIME_FORMATTER_DASH =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String PREFIX_93 = "93";
  private static final String PREFIX_0 = "0";

  private static final String EAST_MONEY_INDEX_REQUEST_URL =
      "https://push2his.eastmoney.com/api/qt/stock/kline/get?secid=%d.%s"
          + "&ut=fa5fd1943c7b386f172d6893dbfba10b"
          + "&fields1=f1%%2Cf2%%2Cf3%%2Cf4%%2Cf5%%2Cf6"
          + "&fields2=f51%%2Cf52%%2Cf53%%2Cf54%%2Cf55%%2Cf56%%2Cf57%%2Cf58%%2Cf59%%2Cf60%%2Cf61"
          + "&klt=10%d&fqt=1&beg=%s&end=20500101&smplmt=100000"
          + "&lmt=1000000&_=%d";

  public List<MarketIndex> getMarketIndex(String indexCode, LocalDate startDate, KLineTypeEnum kType) {
    String formattedStartDate = startDate.format(DATE_FORMATTER);
    try {
      MarketIndexResponse marketIndexResponse =
          getMarketIndex(indexCode, kType, formattedStartDate);
      MarketIndexData data = marketIndexResponse.getData();

      String code = Optional.ofNullable(data).map(MarketIndexData::getCode).orElse(null);
      if (!indexCode.equals(code)) {
        return new ArrayList<>();
      }
      List<String> klines = data.getKlines();

      List<MarketIndex> marketIndices = new ArrayList<>();

      for (String kline : klines) {
        String[] row = kline.split(",");

        MarketIndex rowData = new MarketIndex();
        rowData.setIndexCode(indexCode);
        LocalDate localDate = LocalDate.parse(row[0]);
        rowData.setTradeDate(localDate);

        LocalDateTime localDateTime = parseTradeTime(row[0]);
        rowData.setTradeTime(localDateTime);
        rowData.setOpen(new BigDecimal(row[1]));
        rowData.setClose(new BigDecimal(row[2]));
        rowData.setHigh(new BigDecimal(row[3]));
        rowData.setLow(new BigDecimal(row[4]));
        rowData.setVolume(new BigDecimal(row[5]));
        rowData.setAmount(new BigDecimal(row[6]));
        rowData.setChange(new BigDecimal(row[9]));
        rowData.setChangePct(new BigDecimal(row[8]));
        marketIndices.add(rowData);
      }

      return marketIndices;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private LocalDateTime parseTradeTime(String dateStr) {
    try {
      LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER_DASH);
      return date.atStartOfDay();
    } catch (DateTimeParseException e) {
      return LocalDateTime.parse(dateStr, DATETIME_FORMATTER_DASH);
    }
  }

  private static MarketIndexResponse getMarketIndex(
          String indexCode, KLineTypeEnum kType, String formattedStartDate)
      throws URISyntaxException, IOException {
    int secId = getSecId(indexCode);
    String urlString =
        String.format(
            EAST_MONEY_INDEX_REQUEST_URL,
            secId,
            indexCode,
            kType.getValue(),
            formattedStartDate,
            System.currentTimeMillis());
    URI URI = new URI(urlString);
    URL url = URI.toURL();
    return getMarketIndex(url);
  }

  private static MarketIndexResponse getMarketIndex(URL url) throws IOException {
    HttpURLConnection connection = null;
    BufferedReader in;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(HttpMethod.GET.name());
      connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

      int responseCode = connection.getResponseCode();
      if (responseCode != HttpURLConnection.HTTP_OK) {
        throw new IOException("Failed to retrieve JSON: HTTP error code " + responseCode);
      }

      String contentType = connection.getContentType();
      if (contentType == null || !contentType.contains("application/json")) {
        throw new IOException("Response is not in JSON format");
      }

      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      String responseString = response.toString();
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      return objectMapper.readValue(responseString, MarketIndexResponse.class);
    } catch (IOException e) {
      throw new IOException("Error occurred while fetching JSON from URL: " + url, e);
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static int getSecId(String indexCode) {
    if (indexCode == null || indexCode.isEmpty()) {
      throw new IllegalArgumentException("indexCode should not be null");
    }
    if (indexCode.startsWith(PREFIX_93)) {
      return 2;
    } else if (indexCode.startsWith(PREFIX_0)) {
      return 1;
    }
    return 0;
  }
}
