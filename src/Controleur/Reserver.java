package Controleur;

import Dao.HebergementDAOImpl;
import Dao.ReservationDAOImpl;
import Modele.Hebergement;
import Modele.Reservation;
import Vue.VueAccueil;
import Vue.VueReservation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Reserver implements ActionListener {
    private VueAccueil vueAccueil;
    private HebergementDAOImpl hebergementDAO;
    private ReservationDAOImpl reservationDAO;
    private VueReservation vueReservation;

    public Reserver(VueAccueil vueAccueil, HebergementDAOImpl hebergementDAO, ReservationDAOImpl reservationDAO, VueReservation vueReservation) {
        this.vueAccueil = vueAccueil;
        this.hebergementDAO = hebergementDAO;
        this.reservationDAO = reservationDAO;
        this.vueReservation = vueReservation;

        this.vueReservation.ajouterEcouteur(this);  // Ajouter l'écouteur d'événements
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        // Gestion du bouton "Réserver" pour réserver un hébergement
        if ("VALIDER_RESERVATION".equals(action)) {

            // Ici vous pouvez récupérer les informations de l'hébergement et de la réservation
            reserverHebergement();
        }
    }


    private void reserverHebergement() {
        // Récupérer les informations de réservation depuis la vueReservation
        java.util.Date dateArrivee = vueReservation.getDateArrivee();
        java.util.Date dateDepart = vueReservation.getDateDepart();
        int nbAdultes = vueReservation.getNbAdultes();
        int nbEnfants = vueReservation.getNbEnfants();
        double prixTotal = vueReservation.getPrixTotal();

        // Convertir les java.util.Date en java.sql.Date
        java.sql.Date sqlDateArrivee = new java.sql.Date(dateArrivee.getTime());
        java.sql.Date sqlDateDepart = new java.sql.Date(dateDepart.getTime());

        // Créer une nouvelle réservation
        Reservation reservation = new Reservation(
                Inscription.getUtilisateurId(), // id_utilisateur (en fonction de l'utilisateur connecté)
                vueAccueil.getHebergementSelectionne().getId(), // id hebergement
                sqlDateArrivee,  // Date d'arrivée sous forme de java.sql.Date
                sqlDateDepart,   // Date de départ sous forme de java.sql.Date
                nbAdultes,
                nbEnfants,
                prixTotal,
                "CONFIRMEE",  // Tu peux définir un statut par défaut ou le récupérer d'une autre manière
                new java.sql.Date(System.currentTimeMillis())  // Date de création : date actuelle
        );

        // Ajouter la réservation à la base de données
        reservationDAO.ajouter(reservation);

        // Afficher un message de confirmation dans la vueAccueil
        vueAccueil.afficherMessage("Réservation effectuée pour : du " + sqlDateArrivee + " au " + sqlDateDepart);
        vueReservation.setVisible(false);
        vueAccueil.setVisible(true);
    }

}
