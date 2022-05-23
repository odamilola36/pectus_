package com.lomari.pectus_.utils;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpanseSumDto<T> {

  private T col;

  private BigDecimal sum;

}
