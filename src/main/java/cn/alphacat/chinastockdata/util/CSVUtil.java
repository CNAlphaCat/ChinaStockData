package cn.alphacat.chinastockdata.util;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVUtil {
  public static List<String[]> readCsvWithOpenCSV(Path csvFile, Charset charset) {
    try (Reader reader = Files.newBufferedReader(csvFile, charset)) {
      CSVReader csvReader = new CSVReader(reader);
      return csvReader.readAll();
    } catch (Exception e) {
      log.error("parse csv error", e);
      return new ArrayList<>();
    }
  }
}
