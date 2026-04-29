package com.example.miniprojet;

public class Lieu {

    private String id;
    private String nom;
    private String ville;
    private String description;
    private String imageUrl;
    private String categorie;

    // Constructor vide — obligatoire pour Firestore
    public Lieu() {}

    // Constructor complet
    public Lieu(String id, String nom, String ville,
                String description, String imageUrl, String categorie) {
        this.id          = id;
        this.nom         = nom;
        this.ville       = ville;
        this.description = description;
        this.imageUrl    = imageUrl;
        this.categorie   = categorie;
    }

    // Getters
    public String getId()          { return id; }
    public String getNom()         { return nom; }
    public String getVille()       { return ville; }
    public String getDescription() { return description; }
    public String getImageUrl()    { return imageUrl; }
    public String getCategorie()   { return categorie; }

    // Setters
    public void setId(String id)                   { this.id = id; }
    public void setNom(String nom)                 { this.nom = nom; }
    public void setVille(String ville)             { this.ville = ville; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl)       { this.imageUrl = imageUrl; }
    public void setCategorie(String categorie)     { this.categorie = categorie; }
}