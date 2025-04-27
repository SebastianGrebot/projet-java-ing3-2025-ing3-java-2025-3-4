package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueModifierSupprimerOption extends JFrame {
    private JTextField champId;
    private JTextField champNom;
    private JButton boutonModifier;
    private JButton boutonSupprimer;
    private JButton boutonAccueil;
    private JButton boutonRetour;
    private JButton boutonDeconnexion;

    // Couleurs et polices communes
    private final Color couleurPrincipale = new Color(60, 141, 188);
    private final Color couleurFond = new Color(245, 245, 245);
    private final Font policeNormale = new Font("Arial", Font.PLAIN, 14);
    private final Font policeTitre = new Font("Arial", Font.BOLD, 16);

    public VueModifierSupprimerOption() {
        setTitle("Modifier ou Supprimer une option");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(couleurFond);
        setLayout(new BorderLayout(10, 10));

        // ===== Barre de navigation en haut =====
        JPanel panelNavigation = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelNavigation.setBackground(Color.WHITE);

        boutonAccueil = new JButton("Accueil");
        boutonRetour = new JButton("Retour");
        boutonDeconnexion = new JButton("Déconnexion");

        styliserBoutonNavigation(boutonAccueil);
        styliserBoutonNavigation(boutonRetour);
        styliserBoutonNavigation(boutonDeconnexion);

        panelNavigation.add(boutonAccueil);
        panelNavigation.add(boutonRetour);
        panelNavigation.add(boutonDeconnexion);

        add(panelNavigation, BorderLayout.NORTH);

        // ===== Formulaire principal =====
        champId = new JTextField(10);
        champNom = new JTextField(20);
        boutonModifier = new JButton("Modifier");
        boutonSupprimer = new JButton("Supprimer");

        boutonModifier.setActionCommand("MODIFIER_OPTION");
        boutonSupprimer.setActionCommand("SUPPRIMER_OPTION");

        styliserBouton(boutonModifier);
        styliserBouton(boutonSupprimer);

        JPanel panelForm = new JPanel();
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelForm.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel labelId = new JLabel("ID de l'option :");
        labelId.setFont(policeNormale);
        panelForm.add(labelId, gbc);

        gbc.gridx = 1;
        panelForm.add(champId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelNom = new JLabel("Nouveau nom :");
        labelNom.setFont(policeNormale);
        panelForm.add(labelNom, gbc);

        gbc.gridx = 1;
        panelForm.add(champNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBoutons.setBackground(Color.WHITE);
        panelBoutons.add(boutonModifier);
        panelBoutons.add(boutonSupprimer);
        panelForm.add(panelBoutons, gbc);

        add(panelForm, BorderLayout.CENTER);
    }

    private void styliserBouton(JButton bouton) {
        bouton.setBackground(couleurPrincipale);
        bouton.setForeground(Color.WHITE);
        bouton.setFont(policeNormale);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styliserBoutonNavigation(JButton bouton) {
        bouton.setBackground(new Color(100, 149, 237));
        bouton.setForeground(Color.WHITE);
        bouton.setFont(policeNormale);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters pour récupérer les champs
    public int getIdOption() {
        return Integer.parseInt(champId.getText());
    }

    public String getNouveauNomOption() {
        return champNom.getText();
    }

    // Ajout d'écouteurs pour tous les boutons
    public void ajouterEcouteur(ActionListener listener) {
        boutonModifier.addActionListener(listener);
        boutonSupprimer.addActionListener(listener);
        boutonAccueil.addActionListener(listener);
        boutonRetour.addActionListener(listener);
        boutonDeconnexion.addActionListener(listener);
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void resetChamps() {
        champId.setText("");
        champNom.setText("");
    }

    // Getters pour les boutons navigation si besoin
    public JButton getBoutonAccueil() {
        return boutonAccueil;
    }

    public JButton getBoutonRetour() {
        return boutonRetour;
    }

    public JButton getBoutonDeconnexion() {
        return boutonDeconnexion;
    }
}
