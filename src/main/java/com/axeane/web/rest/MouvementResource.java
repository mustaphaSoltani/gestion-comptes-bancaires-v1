package com.axeane.web.rest;

import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.MouvementDTO;
import com.axeane.domain.util.ResponseUtil;
import com.axeane.service.MouvementService;
import com.axeane.web.util.HeaderUtil;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementResource {
    private final Logger log = LoggerFactory.getLogger(MouvementResource.class);

    private static final String ENTITY_NAME = "mouvement";

    private final MouvementService mouvementService;

    public MouvementResource(MouvementService mouvementService) {
        this.mouvementService = mouvementService;

    }

    @PostMapping
    public ResponseEntity<Mouvement> createMouvement(@Valid @RequestBody MouvementDTO mouvement) throws URISyntaxException {
        log.debug("REST request to save Mouvement : {}", mouvement);
        if (mouvement.getMouvementId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mouvement cannot already have an ID")).body(null);
        }
        Mouvement result = mouvementService.saveMouvement(mouvement);
        return ResponseEntity.created(new URI("/api/mouvements/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<Mouvement> updateClient(@Valid @RequestBody MouvementDTO mouvement) throws URISyntaxException {
        log.debug("REST request to update Mouvement : {}", mouvement);
        if (mouvement.getMouvementId() == null) {
            return createMouvement(mouvement);
        }
        Mouvement result = mouvementService.saveMouvement(mouvement);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mouvement.getMouvementId().toString()))
                .body(result);
    }

    @GetMapping("/numCte/{numC}")
    public ResponseEntity<List<MouvementDTO>> getMouvementByNumCompte(@PathVariable Integer numC) {
        log.debug("REST request to get a page of mouvements");
        List<MouvementDTO> page = mouvementService.findAllMouvementByCompte(numC);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMouvementById(@PathVariable Long id) {
        log.debug("REST request to get Mouvement : {}", id);
        Optional<MouvementDTO> mouvement = Optional.ofNullable(mouvementService.getMouvementById(id));
        return ResponseUtil.wrapOrNotFound(mouvement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        log.debug("REST request to delete Mouvement : {}", id);
        mouvementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
