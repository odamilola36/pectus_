package com.lomari.pectus_.repository;

import com.lomari.pectus_.model.Expanse;
import com.lomari.pectus_.repository.utils.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpanseRepository extends JpaRepository<Expanse, Long>, CustomRepository,
    JpaSpecificationExecutor<Expanse> { }
