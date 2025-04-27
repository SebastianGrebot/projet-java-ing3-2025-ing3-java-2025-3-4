package Vue;

import Modele.Hebergement;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class VueHebergement extends JFrame {
    // Couleurs et polices pour un style cohérent
    private final Color couleurPrincipale = new Color(60, 141, 188);
    private final Color couleurSecondaire = new Color(245, 245, 245);
    private final Color couleurBordure = new Color(220, 220, 220);
    private final Font policeNormale = new Font("Arial", Font.PLAIN, 14);
    private final Font policeTitre = new Font("Arial", Font.BOLD, 16);
    
    private JTable tableHebergements;
    private JScrollPane scrollPane;

    public VueHebergement() {
        setTitle("Liste des hébergements");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal avec une marge
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(panelPrincipal);
        
        // Création du tableau
        String[] entetes = {"ID", "Nom", "Ville", "Pays", "Catégorie", "Prix/nuit (€)"};
        Object[][] donnees = {};
        
        tableHebergements = new JTable(donnees, entetes);
        tableHebergements.setFont(policeNormale);
        tableHebergements.getTableHeader().setFont(policeTitre);
        tableHebergements.getTableHeader().setBackground(couleurPrincipale);
        tableHebergements.getTableHeader().setForeground(Color.WHITE);
        tableHebergements.setRowHeight(30);
        tableHebergements.setGridColor(couleurBordure);
        
        scrollPane = new JScrollPane(tableHebergements);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(couleurBordure),
            "Liste des hébergements disponibles",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            policeTitre,
            couleurPrincipale
        ));
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Affiche un hébergement dans une boîte de dialogue
     * @param hebergement l'hébergement à afficher
     */
    public void afficherHebergement(Hebergement hebergement) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addField(panel, "ID:", String.valueOf(hebergement.getId()));
        addField(panel, "Nom:", hebergement.getNom());
        addField(panel, "Ville:", hebergement.getVille());
        addField(panel, "Pays:", hebergement.getPays());
        addField(panel, "Catégorie:", hebergement.getCategorie());
        addField(panel, "Prix par nuit:", hebergement.getPrixParNuit() + " €");
        addField(panel, "Description:", hebergement.getDescription());
        
        JOptionPane.showMessageDialog(this, panel, 
            "Détails de l'hébergement", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void addField(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(policeNormale);
        panel.add(lbl);
        
        JTextField txt = new JTextField(value);
        txt.setFont(policeNormale);
        txt.setEditable(false);
        txt.setBorder(BorderFactory.createEmptyBorder());
        txt.setBackground(couleurSecondaire);
        panel.add(txt);
    }

    /**
     * Affiche la liste des hébergements dans un tableau
     * @param hebergements la liste des hébergements
     */
    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        String[] entetes = {"ID", "Nom", "Ville", "Pays", "Catégorie", "Prix/nuit (€)"};
        Object[][] donnees = new Object[hebergements.size()][6];
        
        for (int i = 0; i < hebergements.size(); i++) {
            Hebergement h = hebergements.get(i);
            donnees[i][0] = h.getId();
            donnees[i][1] = h.getNom();
            donnees[i][2] = h.getVille();
            donnees[i][3] = h.getPays();
            donnees[i][4] = h.getCategorie();
            donnees[i][5] = h.getPrixParNuit();
        }
        
        tableHebergements.setModel(new javax.swing.table.DefaultTableModel(
            donnees, entetes) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}
