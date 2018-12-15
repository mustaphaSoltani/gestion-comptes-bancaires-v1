package com.axeane.domain.dto;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private Long id;
    private String cin;
    private String name;
    private String prenom;
    private String adresse;
    private String email;
    private String numTel;
    private Set<CompteDTO> comptes = new HashSet<>();

    public ClientDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Set<CompteDTO> getComptes() {
        return comptes;
    }

    public void setComptes(Set<CompteDTO> comptes) {
        this.comptes = comptes;
    }
}
