package Controleur;

import Dao.HebergementDAOImpl;
import Dao.ReservationDAOImpl;
import Modele.Hebergement;
import Modele.Reservation;
import Modele.User;
import Vue.VueAccueil;
import Vue.VuePaiement;
import Vue.VueReservation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class Reserver implements ActionListener {
    private VueAccueil vueAccueil;
    private HebergementDAOImpl hebergementDAO;
    private ReservationDAOImpl reservationDAO;
    private VueReservation vueReservation;
    private VuePaiement vuePaiement;

    private java.util.Date dateArrivee;
    private java.util.Date dateDepart;
    private int nbAdultes;
    private int nbEnfants;
    private double prixTotal;

    public Reserver(VueAccueil vueAccueil, HebergementDAOImpl hebergementDAO, ReservationDAOImpl reservationDAO, VueReservation vueReservation) {
        this.vueAccueil = vueAccueil;
        this.hebergementDAO = hebergementDAO;
        this.reservationDAO = reservationDAO;
        this.vueReservation = vueReservation;

        this.vueReservation.ajouterEcouteur(this);  // Ajoute l'écouteur pour "Valider réservation"
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "VALIDER_RESERVATION":
                lancerVuePaiement(); // Affiche la page de paiement
                break;

            case "PAYER":
                enregistrerReservation(); // Effectue l’enregistrement en base
                vuePaiement.setVisible(false);
                vueAccueil.setVisible(true);
                vueAccueil.afficherMessage("Paiement confirmé. Votre réservation est enregistrée !");
                break;
        }
    }

    private void lancerVuePaiement() {
        // Récupérer les infos de la réservation
        dateArrivee = vueReservation.getDateArrivee();
        dateDepart = vueReservation.getDateDepart();
        nbAdultes = vueReservation.getNbAdultes();
        nbEnfants = vueReservation.getNbEnfants();
        prixTotal = vueReservation.getPrixTotal();

        Hebergement hebergement = vueAccueil.getHebergementSelectionne();
        User user = Inscription.getUtilisateurConnecte();

        vuePaiement = new VuePaiement(user, hebergement, dateArrivee, dateDepart, prixTotal);
        vuePaiement.ajouterEcouteur(this); // Pour écouter le bouton "Payer"
        vueReservation.setVisible(false);
        vuePaiement.setVisible(true);
    }

    private void enregistrerReservation() {
        java.sql.Date sqlDateArrivee = new Date(dateArrivee.getTime());
        java.sql.Date sqlDateDepart = new Date(dateDepart.getTime());

        Reservation reservation = new Reservation(
                Inscription.getUtilisateurId(),
                vueAccueil.getHebergementSelectionne().getId(),
                sqlDateArrivee,
                sqlDateDepart,
                nbAdultes,
                nbEnfants,
                prixTotal,
                "CONFIRMEE",
                new Date(System.currentTimeMillis())
        );

        reservationDAO.ajouter(reservation);
    }
}
