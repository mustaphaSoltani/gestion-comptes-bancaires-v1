package com.axeane.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class CompteDTO {
    private Long id;
    private Integer numCompte;
    private BigDecimal solde;
    @JsonIgnore
    private ClientDTO client;
    @JsonIgnore
    private Long clientId;
    private List<MouvementDTO> mouvements = new LinkedList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(Integer numCompte) {
        this.numCompte = numCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<MouvementDTO> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<MouvementDTO> mouvements) {
        this.mouvements = mouvements;
    }
}
