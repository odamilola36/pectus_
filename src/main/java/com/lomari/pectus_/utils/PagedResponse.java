package com.lomari.pectus_.utils;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Data
@Builder
public class PagedResponse <T> {

  private int page;
  private int size;
  private List<T> content;
  private Long totalElements;
  private int totalPages;
  private boolean first;
  private boolean last;

  public PagedResponse(int page, int size, List<T> content, Long totalElements, int totalPages,
      boolean first, boolean last) {
    this.page = page;
    this.size = size;
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.first = first;
    this.last = last;
  }

  public static <T> PagedResponse<T> pagedResponse(Page<T> page) {
    return new PagedResponse<T>(page.getNumber(), page.getSize(), page.getContent(),
        page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
  }
}
