package Modele;

public class Hebergement {
    private int id;
    private String nom;
    private String description;
    private String adresse;
    private String ville;
    private String pays;
    private int capacite;
    private double prixParNuit;
    private String type;

    // Constructeur
    public Hebergement(int id, String nom, String description, String adresse, String ville, String pays, int capacite, double prixParNuit, String type) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.capacite = capacite;
        this.prixParNuit = prixParNuit;
        this.type = type;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public String getAdresse() { return adresse; }
    public String getVille() { return ville; }
    public String getPays() { return pays; }
    public int getCapacite() { return capacite; }
    public double getPrixParNuit() { return prixParNuit; }
    public String getType() { return type; }

    // Setters (si tu veux les rajouter pour les modifications)
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setVille(String ville) { this.ville = ville; }
    public void setPays(String pays) { this.pays = pays; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setPrixParNuit(double prixParNuit) { this.prixParNuit = prixParNuit; }
    public void setType(String type) { this.type = type; }
}
