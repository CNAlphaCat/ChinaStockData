package cn.alphacat.chinastockdata.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JsonUtil {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static JsonNode parse(String json) throws Exception {
    return OBJECT_MAPPER.readTree(json);
  }

  public static String safeGetText(JsonNode node, String fieldName) {
    return node.has(fieldName) ? node.get(fieldName).asText() : null;
  }

  public static BigDecimal safeGetBigDecimal(JsonNode node, String fieldName) {
    String value = safeGetText(node, fieldName);
    if (value != null && value.matches("-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?")) {
      return new BigDecimal(value);
    }
    return null;
  }

  public static LocalDateTime safeGetLocalDateTime(JsonNode node, String fieldName) {
    String value = safeGetText(node, fieldName);
    if (value != null) {
      try {
        return LocalDateTime.ofEpochSecond(Long.parseLong(value), 0, ZoneOffset.UTC)
            .atZone(ZoneId.of("Asia/Shanghai"))
            .toLocalDateTime();
      } catch (NumberFormatException e) {

      }
    }
    return null;
  }

  public static LocalDate safeGetLocalDateOfPatternyyyyMMdd(JsonNode node, String fieldName) {
    return safeGetLocalDate(node, fieldName, DATE_FORMATTER);
  }

  public static LocalDate safeGetLocalDate(
      JsonNode node, String fieldName, DateTimeFormatter formatter) {
    String value = safeGetText(node, fieldName);
    if (value != null) {
      try {
        return LocalDate.parse(value, formatter);
      } catch (DateTimeParseException e) {

      }
    }
    return null;
  }
}
