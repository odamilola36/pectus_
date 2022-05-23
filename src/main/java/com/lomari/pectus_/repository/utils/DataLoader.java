package com.lomari.pectus_.repository.utils;


import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.repository.ExpanseRepository;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@Slf4j
public class DataLoader {

  enum Headers{
    departments,
    project_name,
    amount,
    date,
    member_name
  }

  private final ExpanseRepository expanseRepository;

  public DataLoader(ExpanseRepository expanseRepository) {
    this.expanseRepository = expanseRepository;
  }

  @PostConstruct
  public void init(){
    persistRecords();
  }

  private List<CSVRecord> parseFile(){
    // read file from data directory
    try {
      InputStream resource = getClass().getResourceAsStream("/expanses.csv");
      Reader in = new InputStreamReader(resource);
      return CSVFormat.DEFAULT
          .withFirstRecordAsHeader()
          .parse(in)
          .getRecords();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void persistRecords() {
    List<CSVRecord> csvRecords = parseFile();

    if ( csvRecords == null || csvRecords.size() == 0 ){
      log.info("There are no records to persist!!!");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    assert csvRecords != null;
    csvRecords.forEach( expanse -> {
        Expanse expanse1 = Expanse.builder()
            .department(expanse.get(Headers.departments))
            .projectName(expanse.get(Headers.project_name))
            .memberName(expanse.get(Headers.member_name))
            .date(LocalDate.parse(expanse.get(Headers.date), formatter))
            .amount(stringToBigDecimal(expanse.get(Headers.amount)))
            .build();
        expanseRepository.save(expanse1);
    });

    log.info("records saved successfully");

  }

  private BigDecimal stringToBigDecimal(String amount) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(',');
    symbols.setDecimalSeparator('.');
    String pattern = "#,##0.0#";
    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
    decimalFormat.setParseBigDecimal(true);

    try {
      return (BigDecimal) decimalFormat.parse(amount);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return BigDecimal.ZERO;
  }

}
