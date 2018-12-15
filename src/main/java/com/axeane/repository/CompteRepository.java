package com.axeane.repository;

import com.axeane.domain.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Compte findByNumCompte(Long numCompte);
}
