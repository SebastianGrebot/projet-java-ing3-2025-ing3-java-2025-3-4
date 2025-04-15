package Controleur;

import Dao.UserDAOImpl;
import Modele.User;
import Vue.VueAdmin;
import Vue.VueConnexion;
import Vue.VueInscription;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Inscription implements ActionListener {

    private UserDAOImpl userDAO;
    private VueInscription vueInscription;
    private VueConnexion vueConnexion;
    private VueAdmin vueAdmin;

    private Accueil accueil;
    private AccueilAdmin accueilAdmin;

    public Inscription(UserDAOImpl userDAO, VueInscription vueInscription, VueConnexion vueConnexion, VueAdmin vueAdmin, Accueil accueil, AccueilAdmin accueilAdmin) {
        this.userDAO = userDAO;
        this.vueInscription = vueInscription;
        this.vueConnexion = vueConnexion;
        this.vueAdmin = vueAdmin;
        this.accueil = accueil;
        this.accueilAdmin = accueilAdmin;

        vueConnexion.setVisible(true);
        vueInscription.setVisible(false);
        vueAdmin.setVisible(false);


        this.vueInscription.ajouterEcouteur(this);
        this.vueConnexion.ajouterEcouteur(this);
        this.vueAdmin.ajouterEcouteur(this);

        ///  rajouter ecouteur pour accueil??
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "SINSCRIPTION":
                // Gérer l'inscription
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
                    accueil.afficherAccueil();
                }
                break;

            case "CONNEXION":
                // Passer à la page de connexion
                vueInscription.setVisible(false);
                vueConnexion.setVisible(true);
                break;

            case "CONNEXION_PAGE":
                // Gérer la connexion
                String emailC = vueConnexion.getEmail();
                String mdpC = vueConnexion.getMotDePasse();

                if (emailC.isEmpty() || mdpC.isEmpty()) {
                    vueConnexion.afficherMessage("Tous les champs doivent être remplis.");
                } else {
                    // quand est ce quon set user a ancien???
                    ///  ici normalement ancien et pas nouveau
                    User user = userDAO.chercher(emailC, mdpC, "nouveau");
                    if (user != null) {
                        vueConnexion.afficherMessage("Heureux de vous revoir " + user.getNom());
                        accueil.afficherAccueil();
                    } else {
                        vueConnexion.afficherMessage("Email ou mot de passe incorrect.");
                    }
                }
                break;

            case "INSCRIPTION_PAGE":
                // Passer à la page d'inscription
                vueConnexion.setVisible(false);
                vueInscription.setVisible(true);
                break;

            case "ADMIN_CONNEXION":
                // Passer à la page Admin
                vueConnexion.setVisible(false);
                vueAdmin.setVisible(true);
                break;

            case "ADMIN":
                // Gérer connexion Admin
                String emailA = vueAdmin.getEmail();
                String mdpA = vueAdmin.getMotDePasse();

                if (emailA.isEmpty() || mdpA.isEmpty()) {
                    vueAdmin.afficherMessage("Tous les champs doivent être remplis.");
                } else {
                    User user = userDAO.chercher(emailA, mdpA, "admin");
                    if (user != null) {
                        vueConnexion.afficherMessage("Heureux de vous revoir " + user.getNom());
                        accueilAdmin.afficherAccueilAdmin();
                    } else {
                        vueConnexion.afficherMessage("Email ou mot de passe incorrect ou vous n'avez pas les droits");
                    }
                }
                break;

            case "RETOUR_CONNEXION":
                // Retour à la page de connexion depuis la page Admin
                vueAdmin.setVisible(false);
                vueConnexion.setVisible(true);
                break;

            case "RETOUR_INSCRIPTION":
                // Retour à la page de connexion depuis la page d'inscription
                vueInscription.setVisible(false);
                vueConnexion.setVisible(true);
                break;

            default:
                vueInscription.afficherMessage("Action inconnue : " + action);
        }
    }
}
