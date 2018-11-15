package com.axeane.domain.dto;

import com.axeane.domain.Compte;
import com.axeane.domain.enumuration.TypeMouvementEnum;

import java.math.BigDecimal;
import java.util.Date;

public class MouvementDTO {
    private Long mouvementId;
    private TypeMouvementEnum mouvementTypeMouvement;
    private BigDecimal mouvementSomme;
    private Date mouvementDate;
    private Long mouvementCompteId;
    private Compte mouvementCompte;

    public MouvementDTO() {
    }

    public Long getMouvementId() {
        return mouvementId;
    }

    public void setMouvementId(Long mouvementId) {
        this.mouvementId = mouvementId;
    }

    public TypeMouvementEnum getMouvementTypeMouvement() {
        return mouvementTypeMouvement;
    }

    public void setMouvementTypeMouvement(TypeMouvementEnum mouvementTypeMouvement) {
        this.mouvementTypeMouvement = mouvementTypeMouvement;
    }

    public Long getMouvementCompteId() {
        return mouvementCompteId;
    }

    public void setMouvementCompteId(Long mouvementCompteId) {
        this.mouvementCompteId = mouvementCompteId;
    }

    public BigDecimal getMouvementSomme() {
        return mouvementSomme;
    }

    public void setMouvementSomme(BigDecimal mouvementSomme) {
        this.mouvementSomme = mouvementSomme;
    }

    public Date getMouvementDate() {
        return mouvementDate;
    }

    public void setMouvementDate(Date mouvementDate) {
        this.mouvementDate = mouvementDate;
    }

    public Compte getMouvementCompte() {
        return mouvementCompte;
    }

    public void setMouvementCompte(Compte mouvementCompte) {
        this.mouvementCompte = mouvementCompte;
    }
}
