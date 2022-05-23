package com.lomari.pectus_.repository.utils;

import com.lomari.pectus_.model.Expanse;
import java.time.LocalDate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ExpanseSpecification implements Specification<Expanse> {

  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Expanse> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    if (criteria.getKey().equalsIgnoreCase("date")) {
      if (criteria.getOperation().equalsIgnoreCase(">")) {
        return criteriaBuilder.greaterThanOrEqualTo(root.<LocalDate>get(criteria.getKey()),
            LocalDate.parse(criteria.getValue().toString()));
      } else if (criteria.getOperation().equalsIgnoreCase("<")) {
        return criteriaBuilder.lessThanOrEqualTo(root.<LocalDate>get(criteria.getKey()),
            LocalDate.parse(criteria.getValue().toString()));
      } else if (criteria.getOperation().equalsIgnoreCase("=")) {
        return criteriaBuilder.equal(root.<LocalDate>get(criteria.getKey()),
            LocalDate.parse(criteria.getValue().toString()));
      }
    }
    if ( criteria.getOperation().equalsIgnoreCase(">") ){
      return criteriaBuilder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
    }
    else if ( criteria.getOperation().equalsIgnoreCase("<") ) {
      return criteriaBuilder.lessThanOrEqualTo(root.<String>get(criteria.getKey()),
          criteria.getValue().toString());
    }
    else {
      return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
    }

  }
}
