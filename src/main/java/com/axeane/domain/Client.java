package com.axeane.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ax_client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.ClientView.class})
    private Long id;

    @NotNull
    @Size(max = 8, min = 8)
    @Column(name = "cin", unique = true, length = 8)
    @JsonView(value = {Views.ClientView.class})
    private String cin;

    @Size(max = 50)
    @Column(name = "nom")
    @JsonView(value = {Views.ClientView.class})
    private String nom;

    @Size(max = 50)
    @Column(name = "prenom")
    @JsonView(value = {Views.ClientView.class})
    private String prenom;

    @Column(name = "adresse")
    @JsonView(value = {Views.ClientView.class})
    private String adresse;

    @Email
    @Size(min = 10, max = 60)
    @Column(name = "email")
    @JsonView(value = {Views.ClientView.class})
    private String email;

    @Size(min = 8, max = 8)
    @Column(name = "num_tel", length = 8)
    @JsonView(value = {Views.ClientView.class})
    private String numTel;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.ALL)
    @JsonView(value = {Views.ClientView.class})
    private Set<Compte> comptes = new HashSet<>();

    public Client() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Client( String cin,String nom, String prenom, String adresse, String email, String numTel, Set<Compte> comptes) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.numTel = numTel;
        this.comptes = comptes;
    }
    public Client( String cin,String nom, String prenom, String adresse, String email, String numTel) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.numTel = numTel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Set<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(Set<Compte> comptes) {
        this.comptes = comptes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "client{" +
                "id=" + id +
                ", nom='" + nom + "'" +
                ", prenom='" + prenom + "'" +
                ", adress='" + adresse + "'" +
                ", email='" + email + "'" +
                ", numTel='" + numTel + "'" +
                '}';
    }
}
