package com.axeane.domain;

public class Mail {

    private String titre;

    private String objet;

    private String message;

    private String text;

    private String urlFile;

    private String email;

    public Mail(String titre, String objet, String message, String text, String urlFile, String email) {
        this.titre = titre;
        this.objet = objet;
        this.message = message;
        this.text = text;
        this.urlFile = urlFile;
        this.email = email;
    }

    public Mail() {
    }

    public String getTitre() {
        return titre;
    }

    public String getObjet() {
        return objet;
    }

    public String getMessage() {
        return message;
    }

    public String getText() {
        return text;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "titre='" + titre + '\'' +
                ", objet='" + objet + '\'' +
                ", message='" + message + '\'' +
                ", text='" + text + '\'' +
                ", urlFile='" + urlFile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
