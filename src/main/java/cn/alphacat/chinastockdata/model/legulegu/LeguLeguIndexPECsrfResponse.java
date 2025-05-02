package cn.alphacat.chinastockdata.model.legulegu;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LeguLeguIndexPECsrfResponse {
  private Map<String, List<String>> cookies;
  private String csrfToken;
}
