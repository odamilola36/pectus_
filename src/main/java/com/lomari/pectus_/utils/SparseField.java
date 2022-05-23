package com.lomari.pectus_.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("expanseFilter")
public class SparseField {

  private String memberName;

  private String projectName;

  private String department;

  private BigDecimal amount;

  private LocalDate date;

}
