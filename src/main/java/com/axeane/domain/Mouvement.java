package com.axeane.domain;

import com.axeane.domain.enumuration.TypeMouvementEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ax_mouvement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mouvement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.CompteView.class, Views.MouvementView.class})
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement")
    @JsonView(value = {Views.MouvementView.class, Views.CompteView.class})
    private TypeMouvementEnum typeMouvement;

    @Positive
    @Column(name = "somme")
    @JsonView(value = {Views.MouvementView.class, Views.CompteView.class})
    private BigDecimal somme;

    @FutureOrPresent
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @JsonView(value = {Views.MouvementView.class, Views.CompteView.class})
    private Date date;

    @NotNull
    @Transient
    @JsonProperty
    private Long compteId;

    @ManyToOne
    @JoinColumn(name = "compte_id")
    private Compte compte;

    public Mouvement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMouvementEnum getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(TypeMouvementEnum typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
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


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mouvement mouvement = (Mouvement) o;
        if (mouvement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mouvement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "compte{" +
                "id=" + id +
                ", typeMouvement='" + typeMouvement + "'" +
                ", somme='" + somme.intValue() + "'" +
                ", date='" + date + "'" +
                '}';
    }
}
