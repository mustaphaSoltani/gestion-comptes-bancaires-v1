package com.axeane.service;

import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import com.axeane.service.exception.GestionCteBancaireException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MouvementService {
    private final Logger log = LoggerFactory.getLogger(MouvementService.class);

    private final MouvementRepository mouvementRepository;
    private final CompteRepository compteRepository;

    public MouvementService(MouvementRepository mouvementRepository, CompteRepository compteRepository) {
        this.mouvementRepository = mouvementRepository;
        this.compteRepository = compteRepository;
    }

    public Mouvement saveMouvement(Mouvement mouvement) throws GestionCteBancaireException {
        log.debug("Request to save Mouvement : {}", mouvement.toString());
        // Request to save Mouvement mouvement@35456
        if (mouvement.getCompteId() == null) { // > 0
            throw new GestionCteBancaireException("vous devez lier le mouvement à un compte");
        }
        Optional<Compte> compte = compteRepository.findById(mouvement.getCompteId());
        if (compte.isPresent()) {
            mouvement.setCompte(compte.get());
        } else {
            throw new GestionCteBancaireException("compte n'existe pas");
        }
        return mouvementRepository.save(mouvement);
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findAllMouvementByCompte(Integer numC) {// get mouvement by compte
        log.debug("Request to get all Mouvements for Compte n°:",numC);
        Compte compte=compteRepository.findByNumCompte(numC);
        return mouvementRepository.findAllByCompte(compte);
    }

    @Transactional(readOnly = true)
    public Mouvement getMouvementById(Long id) {
        log.debug("Request to get Mouvement : {}", id);
        return mouvementRepository.findById(id).get();
    }

    public void delete(Long id) {
        log.debug("Request to delete mouvement : {}", id);
        mouvementRepository.deleteById(id);
    }
}
