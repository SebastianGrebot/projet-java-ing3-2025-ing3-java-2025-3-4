package Controleur;

import Dao.HebergementDAO;
import Modele.Hebergement;
import Vue.VueAccueilAdmin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccueilAdmin implements ActionListener {

    private VueAccueilAdmin vue;
    private HebergementDAO hebergementDAO;

    public AccueilAdmin(VueAccueilAdmin vue, HebergementDAO hebergementDAO) {
        this.vue = vue;
        this.hebergementDAO = hebergementDAO;
        this.vue.ajouterEcouteur(this);
    }

    public void afficherAccueilAdmin() {
        vue.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "AJOUTER_HEBERGEMENT":
                ajouterHebergement();
                break;

            case "DECONNEXION":
                vue.dispose();
                break;
        }
    }

    private void ajouterHebergement() {
        if (vue.getNom().isEmpty() || vue.getAdresse().isEmpty() || vue.getVille().isEmpty() ||
                vue.getPays().isEmpty() || vue.getCategorie().isEmpty() || vue.getPrix().isEmpty() ||
                vue.getDescription().isEmpty() || vue.getPhoto().isEmpty()) {

            vue.afficherMessage("Veuillez remplir tous les champs, y compris la photo !");
            return;
        }

        try {
            String nom = vue.getNom();
            String adresse = vue.getAdresse();
            String ville = vue.getVille();
            String pays = vue.getPays();
            String categorie = vue.getCategorie();
            double prix = Double.parseDouble(vue.getPrix());
            String description = vue.getDescription();
            String photo = vue.getPhoto();  // <- récupération du chemin vers l'image

            Hebergement h = new Hebergement(0, nom, description, adresse, ville, pays, prix, categorie, photo);
            hebergementDAO.ajouter(h);

            vue.afficherMessage("Hébergement ajouté avec succès !");
            vue.resetChamps();

        } catch (NumberFormatException ex) {
            vue.afficherMessage("Le prix doit être un nombre valide.");
        } catch (Exception ex) {
            ex.printStackTrace();
            vue.afficherMessage("Erreur lors de l'ajout de l'hébergement.");
        }
    }
}
