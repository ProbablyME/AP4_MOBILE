package com.example.ap4.Model;

public class AdherentModel {
    private final int id;
    private final String nom;
    private final String prenom;
    private final String mail;

    public AdherentModel(int id, String nom, String prenom, String mail) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
    }
    public int getId()     { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getMail() { return mail; }
}
