package com.eplugger.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJPA<T>
        extends JpaRepository<T, String>,
        JpaSpecificationExecutor<T> {
}