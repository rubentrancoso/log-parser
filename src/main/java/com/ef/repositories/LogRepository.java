package com.ef.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ef.entities.Logline;

@Repository
public interface LogRepository  extends JpaRepository<Logline, Integer>, LogRepositoryCustom  {}
