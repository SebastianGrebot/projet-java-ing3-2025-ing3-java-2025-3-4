package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class VueAccueilAdmin extends JFrame {

    private JTextField champNom;
    private JTextField champAdresse;
    private JTextField champVille;
    private JTextField champPays;
    private JComboBox<String> comboCategorie;
    private JTextField champPrix;
    private JTextArea champDescription;
    private JTextField champPhoto;
    private JButton boutonParcourir;

    private JButton boutonAjouter;
    private JButton boutonDeconnexion;

    public VueAccueilAdmin() {
        setTitle("Accueil Admin - Ajouter un Hébergement");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulaire = new JPanel(new GridLayout(9, 2, 10, 10));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Formulaire d'ajout"));

        champNom = new JTextField();
        champAdresse = new JTextField();
        champVille = new JTextField();
        champPays = new JTextField();

        String[] categories = {"Appartement", "Hôtel", "Villa", "Camping", "Resort"};
        comboCategorie = new JComboBox<>(categories);

        champPrix = new JTextField();
        champDescription = new JTextArea(3, 20);
        champDescription.setLineWrap(true);
        champDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(champDescription);

        champPhoto = new JTextField();
        boutonParcourir = new JButton("Parcourir...");
        boutonParcourir.addActionListener(e -> choisirFichierPhoto());

        JPanel panelPhoto = new JPanel(new BorderLayout(5, 0));
        panelPhoto.add(champPhoto, BorderLayout.CENTER);
        panelPhoto.add(boutonParcourir, BorderLayout.EAST);

        boutonAjouter = new JButton("Ajouter");
        boutonAjouter.setActionCommand("AJOUTER_HEBERGEMENT");

        boutonDeconnexion = new JButton("Déconnexion");
        boutonDeconnexion.setActionCommand("DECONNEXION");

        panelFormulaire.add(new JLabel("Nom :"));
        panelFormulaire.add(champNom);
        panelFormulaire.add(new JLabel("Adresse :"));
        panelFormulaire.add(champAdresse);
        panelFormulaire.add(new JLabel("Ville :"));
        panelFormulaire.add(champVille);
        panelFormulaire.add(new JLabel("Pays :"));
        panelFormulaire.add(champPays);
        panelFormulaire.add(new JLabel("Catégorie :"));
        panelFormulaire.add(comboCategorie);
        panelFormulaire.add(new JLabel("Prix par nuit (€) :"));
        panelFormulaire.add(champPrix);
        panelFormulaire.add(new JLabel("Description :"));
        panelFormulaire.add(scrollDescription);
        panelFormulaire.add(new JLabel("Photo (chemin) :"));
        panelFormulaire.add(panelPhoto);
        panelFormulaire.add(new JLabel(""));
        panelFormulaire.add(boutonAjouter);

        JPanel panelBas = new JPanel();
        panelBas.add(boutonDeconnexion);

        setLayout(new BorderLayout());
        add(panelFormulaire, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);
    }

    private void choisirFichierPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            champPhoto.setText(fichier.getPath());
        }
    }

    // Getters
    public String getNom() { return champNom.getText(); }
    public String getAdresse() { return champAdresse.getText(); }
    public String getVille() { return champVille.getText(); }
    public String getPays() { return champPays.getText(); }
    public String getCategorie() { return (String) comboCategorie.getSelectedItem(); }
    public String getPrix() { return champPrix.getText(); }
    public String getDescription() { return champDescription.getText(); }
    public String getPhoto() { return champPhoto.getText(); }

    public void ajouterEcouteur(ActionListener listener) {
        boutonAjouter.addActionListener(listener);
        boutonDeconnexion.addActionListener(listener);
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void resetChamps() {
        champNom.setText("");
        champAdresse.setText("");
        champVille.setText("");
        champPays.setText("");
        comboCategorie.setSelectedIndex(0);
        champPrix.setText("");
        champDescription.setText("");
        champPhoto.setText("");
    }
}
