package cn.alphacat.chinastockdata.market.handler;

import cn.alphacat.chinastockdata.model.marketindex.IndexConstituent;
import cn.alphacat.chinastockdata.util.LocalDateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MarketIndexConstituentHandler {
  private static final String CSI300_INDEX_CONSTITUENT_EXCEL_URL =
      "https://oss-ch.csindex.com.cn/static/html/csindex/public/uploads/file/autofile/cons/000300cons.xls";
  private static final int CSI_300_EXCEL_DATE_INDEX = 0;
  private static final int CSI_300_EXCEL_INDEXCODE_INDEX = 1;
  private static final int CSI_300_EXCEL_INDEXNAME_INDEX = 2;
  private static final int CSI_300_EXCEL_INDEXNAME_ENG_INDEX = 3;
  private static final int CSI_300_EXCEL_CONSTITUENT_CODE_INDEX = 4;
  private static final int CSI_300_EXCEL_CONSTITUENT_NAME_INDEX = 5;
  private static final int CSI_300_EXCEL_CONSTITUENT_NAME_ENG_INDEX = 6;
  private static final int CSI_300_EXCEL_EXCHANGENAME_INDEX = 7;
  private static final int CSI_300_EXCEL_EXCHANGENAME_ENG_INDEX = 8;

  public List<IndexConstituent> getCSI300IndexConstituent() {
    List<IndexConstituent> constituents = new ArrayList<>();

    try (InputStream inputStream =
            new URI(CSI300_INDEX_CONSTITUENT_EXCEL_URL).toURL().openStream();
        Workbook workbook = WorkbookFactory.create(inputStream)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          continue;
        }

        Date dateCellValue = row.getCell(CSI_300_EXCEL_DATE_INDEX).getDateCellValue();
        LocalDate date = LocalDateUtil.parseDateOfPatternyyyyMMdd(dateCellValue);

        String indexCode = row.getCell(CSI_300_EXCEL_INDEXCODE_INDEX).getStringCellValue();
        String indexName = row.getCell(CSI_300_EXCEL_INDEXNAME_INDEX).getStringCellValue();
        String indexNameEng = row.getCell(CSI_300_EXCEL_INDEXNAME_ENG_INDEX).getStringCellValue();
        String constituentCode =
            row.getCell(CSI_300_EXCEL_CONSTITUENT_CODE_INDEX).getStringCellValue();
        String constituentName =
            row.getCell(CSI_300_EXCEL_CONSTITUENT_NAME_INDEX).getStringCellValue();
        String constituentNameEng =
            row.getCell(CSI_300_EXCEL_CONSTITUENT_NAME_ENG_INDEX).getStringCellValue();
        String exchangeName = row.getCell(CSI_300_EXCEL_EXCHANGENAME_INDEX).getStringCellValue();
        String exchangeNameEng =
            row.getCell(CSI_300_EXCEL_EXCHANGENAME_ENG_INDEX).getStringCellValue();

        IndexConstituent constituent =
            IndexConstituent.builder()
                .date(date)
                .indexCode(indexCode)
                .indexName(indexName)
                .indexNameEng(indexNameEng)
                .constituentCode(constituentCode)
                .constituentName(constituentName)
                .constituentNameEng(constituentNameEng)
                .exchangeName(exchangeName)
                .exchangeNameEng(exchangeNameEng)
                .build();
        constituents.add(constituent);
      }
      return constituents;
    } catch (Exception e) {
      return null;
    }
  }
}
