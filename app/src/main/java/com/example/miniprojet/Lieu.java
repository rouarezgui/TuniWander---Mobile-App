package com.example.miniprojet;

import java.util.List;
import java.util.Map;

public class Lieu {

    private String id;
    private String nom;
    private String ville;
    private String description;
    private String imageUrl;
    private String categorie;
    private List<Map<String, String>> programme;
    private Double rating;
    private Long   nbAvis;
    private String agenceId;
    private String guideId; // Add guideId to link a guide to a tour

    public Lieu() {}

    public Lieu(String id, String nom, String ville, String description,
                String imageUrl, String categorie,
                List<Map<String, String>> programme) {
        this.id          = id;
        this.nom         = nom;
        this.ville       = ville;
        this.description = description;
        this.imageUrl    = imageUrl;
        this.categorie   = categorie;
        this.programme   = programme;
        this.rating      = 0.0;
        this.nbAvis      = 0L;
    }

    public String getId()          { return id; }
    public String getNom()         { return nom; }
    public String getVille()       { return ville; }
    public String getDescription() { return description; }
    public String getImageUrl()    { return imageUrl; }
    public String getCategorie()   { return categorie; }
    public List<Map<String, String>> getProgramme() { return programme; }
    public Double getRating()      { return rating; }
    public Long   getNbAvis()      { return nbAvis; }
    public String getAgenceId()    { return agenceId; }
    public String getGuideId()     { return guideId; }

    public void setId(String id)                   { this.id = id; }
    public void setNom(String nom)                 { this.nom = nom; }
    public void setVille(String ville)             { this.ville = ville; }
    public void setDescription(String d)           { this.description = d; }
    public void setImageUrl(String imageUrl)       { this.imageUrl = imageUrl; }
    public void setCategorie(String categorie)     { this.categorie = categorie; }
    public void setProgramme(List<Map<String, String>> p) { this.programme = p; }
    public void setRating(Double rating)           { this.rating = rating; }
    public void setNbAvis(Long nbAvis)             { this.nbAvis = nbAvis; }
    public void setAgenceId(String agenceId)       { this.agenceId = agenceId; }
    public void setGuideId(String guideId)         { this.guideId = guideId; }
}