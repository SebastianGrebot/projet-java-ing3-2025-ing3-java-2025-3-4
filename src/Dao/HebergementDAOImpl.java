package Dao;

import Modele.Hebergement;
import java.sql.*;
import java.util.ArrayList;

public class HebergementDAOImpl implements HebergementDAO {
    private DaoFactory daoFactory;

    public HebergementDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public ArrayList<Hebergement> getAll() {
        ArrayList<Hebergement> listeHebergements = new ArrayList<>();

        try {
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            ResultSet resultats = statement.executeQuery("SELECT * FROM Hebergements");

            while (resultats.next()) {
                int id = resultats.getInt("hebergement_id");
                String nom = resultats.getString("nom");
                String description = resultats.getString("description");
                String adresse = resultats.getString("adresse");
                String ville = resultats.getString("ville");
                String pays = resultats.getString("pays");
                int capacite = resultats.getInt("capacite");
                double prix = resultats.getDouble("prix_par_nuit");
                String type = resultats.getString("type");

                Hebergement h = new Hebergement(id, nom, description, adresse, ville, pays, capacite, prix, type);
                listeHebergements.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Impossible de récupérer la liste des hébergements");
        }

        return listeHebergements;
    }

    @Override
    public void ajouter(Hebergement h) {
        try {
            Connection connexion = daoFactory.getConnection();

            PreparedStatement ps = connexion.prepareStatement(
                    "INSERT INTO Hebergements (nom, description, adresse, ville, pays, capacite, prix_par_nuit, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, h.getNom());
            ps.setString(2, h.getDescription());
            ps.setString(3, h.getAdresse());
            ps.setString(4, h.getVille());
            ps.setString(5, h.getPays());
            ps.setInt(6, h.getCapacite());
            ps.setDouble(7, h.getPrixParNuit());
            ps.setString(8, h.getType());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de l'hébergement impossible");
        }
    }

    @Override
    public Hebergement chercher(int id) {
        Hebergement h = null;

        try {
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            ResultSet resultats = statement.executeQuery("SELECT * FROM Hebergements WHERE hebergement_id = " + id);

            if (resultats.next()) {
                h = new Hebergement(
                        resultats.getInt("hebergement_id"),
                        resultats.getString("nom"),
                        resultats.getString("description"),
                        resultats.getString("adresse"),
                        resultats.getString("ville"),
                        resultats.getString("pays"),
                        resultats.getInt("capacite"),
                        resultats.getDouble("prix_par_nuit"),
                        resultats.getString("type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hébergement non trouvé");
        }

        return h;
    }

    @Override
    public Hebergement modifier(Hebergement h) {
        try {
            Connection connexion = daoFactory.getConnection();

            PreparedStatement ps = connexion.prepareStatement(
                    "UPDATE Hebergements SET nom = ?, description = ?, adresse = ?, ville = ?, pays = ?, capacite = ?, prix_par_nuit = ?, type = ? WHERE hebergement_id = ?"
            );
            ps.setString(1, h.getNom());
            ps.setString(2, h.getDescription());
            ps.setString(3, h.getAdresse());
            ps.setString(4, h.getVille());
            ps.setString(5, h.getPays());
            ps.setInt(6, h.getCapacite());
            ps.setDouble(7, h.getPrixParNuit());
            ps.setString(8, h.getType());
            ps.setInt(9, h.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification impossible");
        }

        return h;
    }

    @Override
    public void supprimer(Hebergement h) {
        try {
            Connection connexion = daoFactory.getConnection();

            PreparedStatement ps = connexion.prepareStatement(
                    "DELETE FROM Hebergements WHERE hebergement_id = ?"
            );
            ps.setInt(1, h.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression impossible");
        }
    }
}
