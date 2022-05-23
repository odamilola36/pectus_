package com.lomari.pectus_.repository.utils;

import com.lomari.pectus_.model.Expanse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

public class ExpanseSpecificationBuilder {

  private final List<SearchCriteria> params;

  public ExpanseSpecificationBuilder() {
    this.params = new ArrayList<>();
  }

  public ExpanseSpecificationBuilder with(String key, String operation, Object value) {
    params.add(new SearchCriteria(key, operation, value));
    return this;
  }

  public Specification<Expanse> build() {
    if (params.size() == 0) {
      return null;
    }

    List<Specification<Expanse>> specs = params.stream().map(ExpanseSpecification::new)
        .collect(Collectors.toList());

    Specification<Expanse> result = specs.get(0);
    for (int i = 1; i < specs.size(); i++) {
      result = Specification.where(result).and(specs.get(i));
    }

    return result;
  }
}
