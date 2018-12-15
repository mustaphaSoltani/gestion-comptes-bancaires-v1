package com.axeane.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ax_compte")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "num_compte", unique = true)
    private Integer numCompte;

    @Column(name = "solde")
    private BigDecimal solde;

    @Transient
    @JsonProperty
    private Long clientId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private Client client;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compte")
    @JsonIgnore
    private List<Mouvement> mouvements = new LinkedList<>();;

    public Compte() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Mouvement> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<Mouvement> mouvements) {
        this.mouvements = mouvements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compte compte = (Compte) o;
        if (compte.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, compte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "compte{" +
                "id=" + id +
                ", numCompte='" + numCompte + "'" +
                ", solde='" + solde + "'" +
                '}';
    }
}
