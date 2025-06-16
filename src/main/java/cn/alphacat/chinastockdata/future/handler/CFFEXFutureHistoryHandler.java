package cn.alphacat.chinastockdata.future.handler;

import cn.alphacat.chinastockdata.enums.CFFEXFutureHistoryPrefixEnums;
import cn.alphacat.chinastockdata.model.future.CFFEXFutureHistory;
import cn.alphacat.chinastockdata.model.future.OriginalCFFEXFutureHistory;
import cn.alphacat.chinastockdata.util.CSVUtil;
import cn.alphacat.chinastockdata.util.FileUtil;
import cn.alphacat.chinastockdata.util.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CFFEXFutureHistoryHandler {
  private static final String REQUEST_URL = "http://www.cffex.com.cn/sj/historysj/%s/zip/%s.zip";

  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .cookieHandler(new java.net.CookieManager())
          .build();

  public HashMap<LocalDate, List<CFFEXFutureHistory>> getFutureHistory(
      Year year, Month month, List<CFFEXFutureHistoryPrefixEnums> prefixEnums) {
    List<CFFEXFutureHistory> futureHistory = getFutureHistory(year, month);
    List<CFFEXFutureHistory> filterList =
        futureHistory.stream()
            .filter(
                future ->
                    prefixEnums.stream()
                        .anyMatch(prefixEnum -> future.getCode().startsWith(prefixEnum.getValue())))
            .toList();
    HashMap<LocalDate, List<CFFEXFutureHistory>> map = new HashMap<>();
    filterList.forEach(
        future -> {
          if (map.containsKey(future.getDate())) {
            map.get(future.getDate()).add(future);
          } else {
            map.put(future.getDate(), new ArrayList<>(List.of(future)));
          }
        });
    return map;
  }

  public List<CFFEXFutureHistory> getFutureHistory(Year year, Month month) {
    String param = year.toString() + String.format("%02d", month.getValue());
    String url = String.format(REQUEST_URL, param, param);

    HttpRequest request =
        HttpRequest.newBuilder()
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
            .uri(URI.create(url))
            .GET()
            .build();
    List<CFFEXFutureHistory> cffexFutureHistories = new ArrayList<>();
    try {
      HttpResponse<Path> response =
          HTTP_CLIENT.send(
              request,
              HttpResponse.BodyHandlers.ofFile(
                  Files.createTempDirectory("cffex_zip_").resolve("downloaded.zip")));

      if (response.statusCode() != HttpURLConnection.HTTP_OK) {
        return new ArrayList<>();
      }
      Path extractedDir = FileUtil.extractZipFile(response.body());
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(extractedDir, "*.csv")) {
        for (Path csvFile : stream) {
          List<String[]> historyList = CSVUtil.readCsvWithOpenCSV(csvFile, Charset.forName("GBK"));
          String fileName = csvFile.getFileName().toString();
          String dateStr = fileName.substring(0, 8);
          LocalDate date = LocalDateUtil.autoParseDate(dateStr);
          for (String[] history : historyList) {
            OriginalCFFEXFutureHistory originalCffexFutureHistory = getCffexFutureHistory(history);
            CFFEXFutureHistory cffexFutureHistory =
                new CFFEXFutureHistory(originalCffexFutureHistory);
            cffexFutureHistory.setDate(date);
            cffexFutureHistories.add(cffexFutureHistory);
          }
        }
      }
      return cffexFutureHistories;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private static OriginalCFFEXFutureHistory getCffexFutureHistory(String[] history) {
    OriginalCFFEXFutureHistory originalCffexFutureHistory = new OriginalCFFEXFutureHistory();
    originalCffexFutureHistory.setCode(history[0]);
    originalCffexFutureHistory.setOpen(history[1]);
    originalCffexFutureHistory.setHigh(history[2]);
    originalCffexFutureHistory.setLow(history[3]);
    originalCffexFutureHistory.setVolume(history[4]);
    originalCffexFutureHistory.setAmount(history[5]);
    originalCffexFutureHistory.setHoldingVolume(history[6]);
    originalCffexFutureHistory.setHoldingVolumeChange(history[7]);
    originalCffexFutureHistory.setClose(history[8]);
    originalCffexFutureHistory.setSettlementToday(history[9]);
    originalCffexFutureHistory.setSettlementYesterday(history[10]);
    originalCffexFutureHistory.setChange1(history[11]);
    originalCffexFutureHistory.setChange2(history[12]);
    originalCffexFutureHistory.setDelta(history[13]);
    return originalCffexFutureHistory;
  }
}
