package com.axeane.service;

import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.MouvementDTO;
import com.axeane.domain.mapper.MouvementMapper;
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
    private final MouvementMapper mapperMouvement;
    private final MouvementRepository mouvementRepository;
    private final CompteRepository compteRepository;

    public MouvementService(MouvementMapper mapperMouvement, MouvementRepository mouvementRepository, CompteRepository compteRepository) {
        this.mapperMouvement = mapperMouvement;
        this.mouvementRepository = mouvementRepository;
        this.compteRepository = compteRepository;
    }

    public MouvementDTO saveMouvement(MouvementDTO mouvementDTO) throws GestionCteBancaireException {
        log.info("Request to save Mouvement compte= : {}", mouvementDTO.getCompteId());
        Optional<Compte> compte = compteRepository.findById(mouvementDTO.getCompteId());
        if (!compte.isPresent()) {
            throw new GestionCteBancaireException("compte n'existe pas");
        }
        Mouvement mouvement = mapperMouvement.mouvementDTOToMouvement(mouvementDTO);
        mouvement.setCompte(compte.get());
        return mapperMouvement.mouvementToMouvementDTO(mouvementRepository.save(mouvement));
    }

    @Transactional(readOnly = true)
    public List<MouvementDTO> findMouvementByCompte(Long numCompte) {// get mouvement by compte
        log.info("Request to get all Mouvements for Compte n°:{}", numCompte);
        Optional<Compte> compte = compteRepository.findById(numCompte);
        if (!compte.isPresent()) {
            throw new GestionCteBancaireException("compte n'existe pas");
        }
        log.info(" Compte found {}:", compte.toString());
        List<Mouvement> mouvements = mouvementRepository.findAllByCompte(compte.get());
        return mapperMouvement.convertMouvementListToMouvementDTOList(mouvements);
    }

    @Transactional(readOnly = true)
    public MouvementDTO getMouvementById(Long id) {
        log.debug("Request to get Mouvement : {}", id);
        Mouvement mouvement = mouvementRepository.findById(id).get();
        return mapperMouvement.mouvementToMouvementDTO(mouvement);
    }

    public void delete(Long id) {
        log.debug("Request to delete mouvement : {}", id);
        mouvementRepository.deleteById(id);
    }
}
