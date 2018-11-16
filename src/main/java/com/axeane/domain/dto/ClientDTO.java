package com.axeane.domain.dto;

import com.axeane.domain.Compte;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private Long clientId;
    private String clientCin;
    private String clientName;
    private String clientPrenom;
    private String clientAdresse;
    private String clientEmail;
    private String clientNumTel;
    private Set<CompteDTO> clientComptes = new HashSet<>();

    public ClientDTO() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientCin() {
        return clientCin;
    }

    public void setClientCin(String clientCin) {
        this.clientCin = clientCin;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPrenom() {
        return clientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public String getClientAdresse() {
        return clientAdresse;
    }

    public void setClientAdresse(String clientAdresse) {
        this.clientAdresse = clientAdresse;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientNumTel() {
        return clientNumTel;
    }

    public void setClientNumTel(String clientNumTel) {
        this.clientNumTel = clientNumTel;
    }

    public Set<CompteDTO> getClientComptes() {
        return clientComptes;
    }

    public void setClientComptes(Set<CompteDTO> clientComptes) {
        this.clientComptes = clientComptes;
    }
}
