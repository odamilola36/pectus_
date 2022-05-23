package com.lomari.pectus_.repository.utils;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.utils.ExpanseSumDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  @Transactional
  public List<ExpanseSumDto> getSum(String by) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ExpanseSumDto> criteriaQuery = cb.createQuery(ExpanseSumDto.class);
    Root<Expanse> from = criteriaQuery.from(Expanse.class);

    criteriaQuery.multiselect(from.get(by).alias("col"), cb.sum(from.get("amount")).alias("sum"))
        .groupBy(from.get(by));
    return em.createQuery(criteriaQuery).getResultList();
  }

}
