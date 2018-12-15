package com.axeane.domain.dto;

import com.axeane.domain.enumuration.TypeMouvementEnum;

import java.math.BigDecimal;
import java.util.Date;

public class MouvementDTO {
    private Long Id;
    private TypeMouvementEnum typeMouvement;
    private BigDecimal somme;
    private Date date;
    private Long compteId;

    public MouvementDTO() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }
}
