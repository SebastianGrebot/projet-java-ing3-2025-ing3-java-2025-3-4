package Vue;

import Modele.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VueUser extends JFrame {
    
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    
    public VueUser() {
        setTitle("Liste des Clients");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // Création du modèle de table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Email"}, 0);
        tableUsers = new JTable(tableModel);
        tableUsers.setFillsViewportHeight(true);
        tableUsers.setRowHeight(25);
        tableUsers.getTableHeader().setReorderingAllowed(false);
        tableUsers.setFont(new Font("Arial", Font.PLAIN, 14));
        tableUsers.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableUsers.setSelectionBackground(new Color(220, 230, 241));
        tableUsers.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(tableUsers);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Affiche un seul client (peu utilisé ici, mais reste possible)
     */
    public void afficherClient(User user) {
        tableModel.addRow(new Object[]{user.getId(), user.getNom(), user.getEmail()});
    }

    /**
     * Affiche une liste complète de clients
     */
    public void afficherListeClients(ArrayList<User> users) {
        tableModel.setRowCount(0); // Clear table avant d'ajouter
        for (User user : users) {
            afficherClient(user);
        }
    }
}
