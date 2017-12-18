package com.ef.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ef.entities.Bannedip;

@Repository
public interface BannedipRepository  extends JpaRepository<Bannedip, String>  {}
