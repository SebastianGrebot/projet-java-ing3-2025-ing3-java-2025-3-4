package Controleur;

import Dao.UserDAOImpl;
import Modele.User;
import Vue.VueConnexion;
import Vue.VueInscription;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inscription implements ActionListener {

    private UserDAOImpl userDAO;
    private VueInscription vueInscription;
    private VueConnexion vueConnexion;

    public Inscription(UserDAOImpl userDAO, VueInscription vueInscription, VueConnexion vueConnexion) {
        this.userDAO = userDAO;
        this.vueInscription = vueInscription;
        this.vueConnexion = vueConnexion;

        // on lie les boutons ici
        this.vueInscription.ajouterEcouteur(this);
        this.vueConnexion.ajouterEcouteur(this);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            // Cas pour l'inscription dans la page d'inscription
            case "SINSCRIPTION":
                String nom = vueInscription.getNom();
                String prenom = vueInscription.getPrenom();
                String email = vueInscription.getEmail();
                String mdp = vueInscription.getMotDePasse();

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                    vueInscription.afficherMessage("Tous les champs doivent être remplis.");
                } else {
                    User user = new User(prenom, nom, email, mdp, "nouveau");
                    userDAO.ajouter(user);
                    vueInscription.afficherMessage("Bienvenue " + prenom + " ! Inscription réussie");
                }
                break;

            // Cas pour la redirection vers la page de connexion depuis la page d'inscription
            case "CONNEXION":
                vueInscription.setVisible(false);  // On cache la fenêtre d'inscription
                vueConnexion.setVisible(true);    // On montre la fenêtre de connexion
                break;

            // Cas pour la connexion dans la page de connexion
            case "CONNEXION_PAGE":
                String emailC = vueConnexion.getEmail();
                String mdpC = vueConnexion.getMotDePasse();

                if (emailC.isEmpty() || mdpC.isEmpty()) {
                    vueConnexion.afficherMessage("Tous les champs doivent être remplis.");
                } else {
                    User user = new User(emailC, mdpC, "ancien");
                    vueConnexion.afficherMessage("Heureux de vous revoir " + user.getNom());
                }
                break;

            // Cas pour la redirection vers la page d'inscription depuis la page de connexion
            case "INSCRIPTION_PAGE":
                vueConnexion.setVisible(false);  // On cache la fenêtre de connexion
                vueInscription.setVisible(true); // On montre la fenêtre d'inscription
                break;

            // Cas par défaut pour gérer les actions inconnues
            default:
                vueInscription.afficherMessage("Action inconnue : " + action);
        }
    }
}
