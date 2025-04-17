package Controleur;

// import des packages
import Dao.*;
import Modele.*;
import Vue.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Déclaration et instanciation des objets des classes DaoFactory, ProduitDAOImpl, VueProduit,
        // ClientDAOImpl, VueClient, CommanderDAOImpl et VueCommander
        DaoFactory dao = DaoFactory.getInstance("BookingApp", "root", "");

        HebergementDAOImpl daoHebergement = new HebergementDAOImpl(dao);
        VueHebergement vueHebergement = new VueHebergement();
        UserDAOImpl daoUser = new UserDAOImpl(dao);
        VueUser vueUser = new VueUser();


        // Récupérer la liste des produits de la base de données avec l'objet prodao de la classe ProduitDAOImpl
        ArrayList<Hebergement> hebergements = daoHebergement.getAll();

        // Afficher la liste des produits récupérés avec l'objet vuepro de la classe VueProduit
        vueHebergement.afficherListeProduits(hebergements);

        // Récupérer la liste des clients de la base de données avec l'objet clidao de la classe ClientDAOImpl
        ArrayList<User> users = daoUser.getAll();

        // Afficher la liste des clients récupérés avec l'objet vuecli de la classe VueClient
        vueUser.afficherListeClients(users);



        ///  inscription
        VueInscription vueInscription = new VueInscription();
        VueConnexion vueConnexion = new VueConnexion();
        VueAdmin vueAdmin = new VueAdmin();


        ///  accueil client
        VueAccueil vueAccueil = new VueAccueil();

        ReservationDAOImpl daoReservation = new ReservationDAOImpl(dao);
        VueReservation vueReservation = new VueReservation(vueAccueil);
        //  reservation
        Reserver reserver = new Reserver(vueAccueil, daoHebergement, daoReservation, vueReservation);

        Accueil accueil = new Accueil(vueAccueil, daoHebergement , vueConnexion, reserver, vueReservation, daoReservation);

        /// accueil admin
        VueAccueilAdmin vueAccueilAdmin = new VueAccueilAdmin();
        AccueilAdmin accueilAdmin = new AccueilAdmin(vueAccueilAdmin, daoHebergement);

        new Inscription(daoUser, vueInscription, vueConnexion, vueAdmin, accueil, accueilAdmin);



        // Fermer ma connexion
        dao.disconnect();
    }
}