package Controleur;

import Dao.HebergementDAOImpl;
import Dao.OptionDAOImpl;
import Modele.Hebergement;
import Modele.Option;
import Vue.*;
import java.util.List;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccueilAdmin implements ActionListener {

    private VueAccueilAdmin vue;
    private OptionDAOImpl optionDAO;
    private HebergementDAOImpl hebergementDAO;
    private VueAjoutHebergement vueAjoutHebergement;
    private VueAjouterOption vueAjouterOption;
    private VueModifierSupprimerOption vueModifierSupprimerOption;
    private VueAssocierOptionsHebergement vueAssocierOptionsHebergement;

    public AccueilAdmin(VueAccueilAdmin vue,
                        VueAjoutHebergement vueAjoutHebergement,
                        VueAjouterOption vueAjouterOption,
                        VueModifierSupprimerOption vueModifierSupprimerOption,
                        VueAssocierOptionsHebergement vueAssocierOptionsHebergement,
                        OptionDAOImpl optionDAO,
                        HebergementDAOImpl hebergementDAO) {

        this.vue = vue;
        this.vueAjoutHebergement = vueAjoutHebergement;
        this.vueAjouterOption = vueAjouterOption;
        this.vueModifierSupprimerOption = vueModifierSupprimerOption;
        this.vueAssocierOptionsHebergement = vueAssocierOptionsHebergement;
        this.optionDAO = optionDAO;
        this.hebergementDAO = hebergementDAO;

        // Affichage initial
        //vue.setVisible(true);
        vueAjoutHebergement.setVisible(false);
        vueAjouterOption.setVisible(false);
        vueModifierSupprimerOption.setVisible(false);
        vueAssocierOptionsHebergement.setVisible(false);

        // Écouteurs
        vue.ajouterEcouteur(this);
        vueAjoutHebergement.ajouterEcouteur(this);
        vueAjouterOption.ajouterEcouteur(this);
        vueModifierSupprimerOption.ajouterEcouteur(this);
        vueAssocierOptionsHebergement.ajouterEcouteur(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "CREER_HEBERGEMENT":
                vue.setVisible(false);
                vueAjoutHebergement.setVisible(true);
                break;

            case "AJOUTER_HEBERGEMENT":

                if (vueAjoutHebergement.getNom().isEmpty() || vueAjoutHebergement.getAdresse().isEmpty() || vueAjoutHebergement.getVille().isEmpty() ||
                        vueAjoutHebergement.getPays().isEmpty() || vueAjoutHebergement.getCategorie().isEmpty() || vueAjoutHebergement.getPrix().isEmpty() ||
                        vueAjoutHebergement.getDescription().isEmpty() || vueAjoutHebergement.getPhoto().isEmpty()) {

                    vue.afficherMessage("Veuillez remplir tous les champs, y compris la photo !");
                    return;
                }

                try {
                    String nom = vueAjoutHebergement.getNom();
                    String adresse = vueAjoutHebergement.getAdresse();
                    String ville = vueAjoutHebergement.getVille();
                    String pays = vueAjoutHebergement.getPays();
                    String categorie = vueAjoutHebergement.getCategorie();
                    double prix = Double.parseDouble(vueAjoutHebergement.getPrix());
                    String description = vueAjoutHebergement.getDescription();
                    String photo = vueAjoutHebergement.getPhoto();  // <- récupération du chemin vers l'image

                    Hebergement h = new Hebergement(0, nom, description, adresse, ville, pays, prix, categorie, photo);
                    hebergementDAO.ajouter(h);

                    vueAjoutHebergement.afficherMessage("Hébergement ajouté avec succès !");
                    vueAjoutHebergement.resetChamps();

                } catch (NumberFormatException ex) {
                    vueAjoutHebergement.afficherMessage("Le prix doit être un nombre valide.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    vueAjoutHebergement.afficherMessage("Erreur lors de l'ajout de l'hébergement.");
                }
                break;

            case "CREER_OPTION":
                vue.setVisible(false);
                vueAjouterOption.setVisible(true);
                break;

            case "AJOUTER_OPTION":
                String nomOption = vueAjouterOption.getNomOption();
                if (nomOption.isEmpty()) {
                    vueAjouterOption.afficherMessage("Le nom de l'option ne peut pas être vide.");
                } else {
                    optionDAO.ajouter(new Option(nomOption));
                    vueAjouterOption.afficherMessage("Option ajoutée avec succès !");
                    vueAjouterOption.resetChamps();
                }
                break;

            case "MODIFIER_SUPPRIMER_OPTION":
                vue.setVisible(false);
                vueModifierSupprimerOption.setVisible(true);
                break;

            case "MODIFIER_OPTION":
                int id = vueModifierSupprimerOption.getIdOption();
                String nouveauNom = vueModifierSupprimerOption.getNouveauNomOption();
                if (nouveauNom.isEmpty()) {
                    vueModifierSupprimerOption.afficherMessage("Le nom ne peut pas être vide.");
                } else {
                    optionDAO.modifier(new Option(id, nouveauNom));
                    vueModifierSupprimerOption.afficherMessage("Option modifiée !");
                    vueModifierSupprimerOption.resetChamps();
                }
                break;

            case "SUPPRIMER_OPTION":
                int idSupp = vueModifierSupprimerOption.getIdOption();
                optionDAO.supprimer(new Option(idSupp, ""));
                vueModifierSupprimerOption.afficherMessage("Option supprimée !");
                break;

            case "ASSOCIER_OPTION":
                vue.setVisible(false);
                List<Hebergement> hebergements = hebergementDAO.getAll();
                vueAssocierOptionsHebergement.setHebergements(hebergements);

                if (!hebergements.isEmpty()) {
                    Hebergement premier = hebergements.get(0);
                    List<Option> toutesOptions = optionDAO.getAll();
                    List<Option> optionsAssociees = optionDAO.getOptionsPourHebergement(premier.getId());
                    vueAssocierOptionsHebergement.setOptions(toutesOptions, optionsAssociees);
                }

                // 💡 Ici on ajoute le listener pour mettre à jour les options quand on change d'hébergement
                vueAssocierOptionsHebergement.getComboBoxHebergements().addActionListener(evt -> {
                    Hebergement selectionne = vueAssocierOptionsHebergement.getHebergementSelectionne();
                    if (selectionne != null) {
                        List<Option> toutesOptions = optionDAO.getAll();  // Récupérer toutes les options
                        List<Option> optionsAssociees = optionDAO.getOptionsPourHebergement(selectionne.getId()); // Récupérer les options déjà associées

                        // Appeler setOptions avec les options et celles déjà associées
                        vueAssocierOptionsHebergement.setOptions(toutesOptions, optionsAssociees);
                    }
                });


                vueAssocierOptionsHebergement.setVisible(true);
                break;


            case "VALIDER_ASSOCIATION":
                Hebergement heb = vueAssocierOptionsHebergement.getHebergementSelectionne();
                List<Option> selectionnees = vueAssocierOptionsHebergement.getOptionsSelectionnees();

                optionDAO.reinitialiserOptionsPourHebergement(heb.getId());
                for (Option opt : selectionnees) {
                    optionDAO.attribuerOptionAHebergement(heb.getId(), opt.getId());
                }

                vueAssocierOptionsHebergement.afficherMessage("Associations mises à jour !");
                break;


            case "RETOUR_ACCUEIL":
                vueAjoutHebergement.setVisible(false);
                vueAjouterOption.setVisible(false);
                vueModifierSupprimerOption.setVisible(false);
                vueAssocierOptionsHebergement.setVisible(false);
                vue.setVisible(true);
                break;

            case "DECONNEXION":
                vue.dispose();
                break;

            default:
                vue.afficherMessage("Action inconnue : " + action);
        }
    }
    public void afficherAccueilAdmin() {
        vue.setVisible(true);
    }

}

