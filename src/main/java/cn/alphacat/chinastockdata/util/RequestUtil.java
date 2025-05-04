package cn.alphacat.chinastockdata.util;

import java.util.Map;

public class RequestUtil {
  public static String buildUrlByParametersMap(String baseUrl, Map<String, String> requestMap) {
    StringBuilder sb = new StringBuilder();
    sb.append(baseUrl);
    sb.append("?");
    for (Map.Entry<String, String> entry : requestMap.entrySet()) {
      sb.append(entry.getKey());
      sb.append("=");
      sb.append(entry.getValue());
      sb.append("&");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }
}
