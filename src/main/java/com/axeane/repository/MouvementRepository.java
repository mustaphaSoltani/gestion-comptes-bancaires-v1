package com.axeane.repository;

import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Long> {
    List<Mouvement> findAllByCompte(Compte compte);
}
