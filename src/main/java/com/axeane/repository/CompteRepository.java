package com.axeane.repository;

import com.axeane.domain.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Compte findByNumCompte(Integer numCompte);
}
