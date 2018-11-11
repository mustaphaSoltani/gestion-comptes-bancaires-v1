package com.axeane.models;

import com.axeane.domain.enumuration.TypeMouvementEnum;

import java.math.BigDecimal;
import java.util.Date;

public class MouvementModel {
    private BigDecimal somme;
    private Date date;
    private TypeMouvementEnum typeMouvement;
    private BigDecimal solde;

    public MouvementModel(BigDecimal somme, Date date, TypeMouvementEnum typeMouvement, BigDecimal solde) {
        this.somme = somme;
        this.date = date;
        this.typeMouvement = typeMouvement;
        this.solde = solde;
    }

    public MouvementModel() {

    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public TypeMouvementEnum getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(TypeMouvementEnum typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public BigDecimal getSomme() {
        return somme;
    }

    public void setSomme(BigDecimal somme) {
        this.somme = somme;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
