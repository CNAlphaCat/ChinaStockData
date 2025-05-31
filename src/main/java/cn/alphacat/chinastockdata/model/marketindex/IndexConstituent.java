package cn.alphacat.chinastockdata.model.marketindex;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class IndexConstituent {
  private LocalDate date;
  private String indexCode;
  private String indexName;
  private String indexNameEng;
  private String constituentCode;
  private String constituentName;
  private String constituentNameEng;
  private String exchangeName;
  private String exchangeNameEng;
}
