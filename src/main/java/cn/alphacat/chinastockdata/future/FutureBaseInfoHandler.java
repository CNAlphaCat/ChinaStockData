package cn.alphacat.chinastockdata.future;

import cn.alphacat.chinastockdata.enums.EastMoneyQuoteFieldEnum;
import cn.alphacat.chinastockdata.model.FutureMarketOverview;
import cn.alphacat.chinastockdata.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class FutureBaseInfoHandler {
    public List<FutureMarketOverview> getFuturesBaseInfo() {
        String fs = "m:113,m:114,m:115,m:8,m:142,m:225";

        HttpResponse<String> response = getByPage(1, 200, fs);
        if (response == null) {
            return null;
        }

        JsonNode jsonResponse;
        try {
            jsonResponse = JsonUtil.parse(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JsonNode dataNode = jsonResponse.get("data");

        int total = dataNode.get("total").asInt();
        int pz = dataNode.get("diff").size();

        int div = total / pz;
        int mod = total % pz;
        int pages = (mod != 0) ? div + 1 : div;

        try (ExecutorService executor =
                     Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Callable<JsonNode>> tasks = new ArrayList<>();
            for (int i = 1; i <= pages; i++) {
                final int page = i;
                tasks.add(
                        () -> {
                            HttpResponse<String> pageResponse = getByPage(page, pz, fs);
                            return JsonUtil.parse(pageResponse.body());
                        });
            }

            try {
                List<Future<JsonNode>> futures = executor.invokeAll(tasks);
                List<FutureMarketOverview> allData = new ArrayList<>();
                for (Future<JsonNode> future : futures) {
                    JsonNode pageJsonResponse = future.get();
                    JsonNode pageDataNode = pageJsonResponse.get("data");
                    JsonNode diffNode = pageDataNode.get("diff");
                    for (JsonNode item : diffNode) {
                        FutureMarketOverview futureMarketOverview = new FutureMarketOverview(item);
                        allData.add(futureMarketOverview);
                    }
                }
                return allData;
            } catch (InterruptedException | ExecutionException e) {
                return null;
            } finally {
                executor.shutdown();
            }
        }
    }

    private static HttpResponse<String> getByPage(int pn, int pz, String fs) {

        try {
            String fields =
                    Arrays.stream(EastMoneyQuoteFieldEnum.values())
                            .map(EastMoneyQuoteFieldEnum::getKey)
                            .collect(Collectors.joining(","));

            String url =
                    "http://push2.eastmoney.com/api/qt/clist/get?"
                            + "pn="
                            + pn
                            + "&pz="
                            + pz
                            + "&po=1"
                            + "&np=1"
                            + "&fltt=2"
                            + "&invt=2"
                            + "&fid=f12"
                            + "&fs="
                            + fs
                            + "&fields="
                            + fields;
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
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }
}
