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
    private Set<CompteDTO> comptes = new HashSet<>();

    public ClientDTO() {
    }

    public ClientDTO(Long clientId, String clientCin, String clientName, String clientPrenom, String clientAdresse, String clientEmail, String clientNumTel, Set<CompteDTO> comptesClient) {
        this.clientId = clientId;
        this.clientCin = clientCin;
        this.clientName = clientName;
        this.clientPrenom = clientPrenom;
        this.clientAdresse = clientAdresse;
        this.clientEmail = clientEmail;
        this.clientNumTel = clientNumTel;
        this.comptes = comptesClient;
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

    public Set<CompteDTO> getComptes() {
        return comptes;
    }

    public void setComptes(Set<CompteDTO> clientComptes) {
        this.comptes = clientComptes;
    }
}
