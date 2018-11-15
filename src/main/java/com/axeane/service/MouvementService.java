package com.axeane.service;

import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.CompteDTO;
import com.axeane.domain.dto.MouvementDTO;
import com.axeane.domain.mapper.CompteMapper;
import com.axeane.domain.mapper.MouvementMapper;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import com.axeane.service.exception.GestionCteBancaireException;
import org.mapstruct.factory.Mappers;
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
    private MouvementMapper mapperClient = Mappers.getMapper(MouvementMapper.class);
    private CompteMapper mapperCompte = Mappers.getMapper(CompteMapper.class);
    private final MouvementRepository mouvementRepository;
    private final CompteRepository compteRepository;

    public MouvementService(MouvementRepository mouvementRepository, CompteRepository compteRepository) {
        this.mouvementRepository = mouvementRepository;
        this.compteRepository = compteRepository;
    }

    public Mouvement saveMouvement(MouvementDTO mouvementDTO) throws GestionCteBancaireException {
        log.debug("Request to save Mouvement : {}", mouvementDTO.toString());
        if (mouvementDTO.getMouvementCompteId() == null) { // > 0
            throw new GestionCteBancaireException("vous devez lier le mouvement à un compte");
        }
        Optional<Compte> compte = compteRepository.findById(mouvementDTO.getMouvementCompteId());

       if (compte.isPresent()) {
            mouvementDTO.setMouvementCompte(compte.get());
        } else {
            throw new GestionCteBancaireException("compte n'existe pas");
        }
        //Compte compte1=mapperCompte.compteDTOToCompte(compte)
        Mouvement mouvement=mapperClient.mouvementDTOToMouvement(mouvementDTO);
        return mouvementRepository.save(mouvement);
    }

    @Transactional(readOnly = true)
    public List<MouvementDTO> findAllMouvementByCompte(Integer numC) {// get mouvement by compte
        log.debug("Request to get all Mouvements for Compte n°:", numC);
        Compte compte = compteRepository.findByNumCompte(numC);
        List<Mouvement> mouvements= mouvementRepository.findAllByCompte(compte);
        CompteDTO compteDTO=mapperCompte.compteToCompteDTO(compte);
        List<MouvementDTO> mouvementsDTO = mapperClient.convertMouvementListToMouvementDTOList(mouvements);
       // mouvementsDTO.iterator().next().setMouvementCompte(compteDTO);
        return mouvementsDTO;
    }

    @Transactional(readOnly = true)
    public MouvementDTO getMouvementById(Long id) {
        log.debug("Request to get Mouvement : {}", id);
        Mouvement mouvement= mouvementRepository.findById(id).get();
        return mapperClient.mouvementToMouvementDTO(mouvement);
    }

    public void delete(Long id) {
        log.debug("Request to delete mouvement : {}", id);
        mouvementRepository.deleteById(id);
    }
}
