package com.axeane.domain.dto;

import com.axeane.domain.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

public class CompteDTO {
    private Long compteId;
    private Integer compteNumCompte;
    private BigDecimal soldeDTO;
    @JsonIgnore
    private ClientDTO compteClient;
    private Long compteClientId;

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public Integer getCompteNumCompte() {
        return compteNumCompte;
    }

    public void setCompteNumCompte(Integer compteNumCompte) {
        this.compteNumCompte = compteNumCompte;
    }

    public ClientDTO getCompteClient() {
        return compteClient;
    }

    public void setCompteClient(ClientDTO compteClient) {
        this.compteClient = compteClient;
    }

    public BigDecimal getSoldeDTO() {
        return soldeDTO;
    }

    public void setSoldeDTO(BigDecimal soldeDTO) {
        this.soldeDTO = soldeDTO;
    }

    public Long getCompteClientId() {
        return compteClientId;
    }

    public void setCompteClientId(Long compteClientId) {
        this.compteClientId = compteClientId;
    }
}
