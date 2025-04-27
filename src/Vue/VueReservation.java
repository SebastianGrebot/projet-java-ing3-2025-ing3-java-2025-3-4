package Vue;

import Controleur.Inscription;
import Dao.HebergementDAOImpl;
import Modele.Hebergement;
import Modele.Reduction;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VueReservation extends JFrame {

    private JSpinner dateArriveeSpinner;
    private JSpinner dateDepartSpinner;
    private JSpinner spinnerNbAdultes;
    private JSpinner spinnerNbEnfants;
    private JTextField champPrixParNuit;
    private JTextField champPrixTotal;
    private JLabel labelReduction;
    private JButton btnValider;
    private JLabel labelMessage;

    private VueAccueil vueAccueil;
    private HebergementDAOImpl hebergementDAO;

    private final Color couleurPrincipale = new Color(60, 141, 188);
    private final Color couleurFond = new Color(245, 245, 245);

    public VueReservation(VueAccueil vueAccueil, HebergementDAOImpl hebergementDAO) {
        this.vueAccueil = vueAccueil;
        this.hebergementDAO = hebergementDAO;

        setTitle("Réservation");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(couleurFond);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setOpaque(false);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        panelForm.add(creerLigne("Date d'arrivée :", dateArriveeSpinner = createDateSpinner(0)));
        panelForm.add(creerLigne("Date de départ :", dateDepartSpinner = createDateSpinner(1)));
        panelForm.add(creerLigne("Nombre d'adultes :", spinnerNbAdultes = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1))));
        panelForm.add(creerLigne("Nombre d'enfants :", spinnerNbEnfants = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1))));
        panelForm.add(creerLigne("Prix par nuit (€) :", champPrixParNuit = createTextField()));
        panelForm.add(creerLigne("Réduction appliquée :", labelReduction = new JLabel("-")));
        labelReduction.setFont(new Font("Arial", Font.PLAIN, 14));
        labelReduction.setForeground(Color.BLACK);
        panelForm.add(creerLigne("Prix total (€) :", champPrixTotal = createTextField()));

        champPrixParNuit.setEditable(false);
        champPrixTotal.setEditable(false);

        labelMessage = new JLabel("", SwingConstants.CENTER);
        labelMessage.setForeground(Color.RED);
        labelMessage.setFont(new Font("Arial", Font.BOLD, 14));
        panelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        panelForm.add(labelMessage);

        btnValider = new JButton("Valider la réservation");
        btnValider.setActionCommand("VALIDER_RESERVATION");
        styliserBouton(btnValider);
        JPanel boutonPanel = new JPanel();
        boutonPanel.setBackground(Color.WHITE);
        boutonPanel.add(btnValider);

        panelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        panelForm.add(boutonPanel);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        add(panelPrincipal);

        // Listeners pour recalculer automatiquement
        ChangeListener recalculListener = e -> calculerPrixTotal();
        dateArriveeSpinner.addChangeListener(recalculListener);
        dateDepartSpinner.addChangeListener(recalculListener);
        spinnerNbAdultes.addChangeListener(recalculListener);
        spinnerNbEnfants.addChangeListener(recalculListener);

        // Calcul initial au chargement
        SwingUtilities.invokeLater(this::calculerPrixTotal);
    }

    private JPanel creerLigne(String labelTexte, JComponent composant) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelTexte);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, BorderLayout.WEST);
        panel.add(composant, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return panel;
    }

    private JSpinner createDateSpinner(int daysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SpinnerDateModel model = new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd-MM-yyyy"));
        return spinner;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private void styliserBouton(JButton bouton) {
        bouton.setBackground(couleurPrincipale);
        bouton.setForeground(Color.WHITE);
        bouton.setFont(new Font("Arial", Font.BOLD, 16));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setPreferredSize(new Dimension(220, 40));
    }

    private void calculerPrixTotal() {
        try {
            Hebergement h = vueAccueil.getHebergementSelectionne();
            if (h == null) {
                champPrixParNuit.setText("");
                champPrixTotal.setText("");
                labelMessage.setText("Aucun hébergement sélectionné.");
                labelReduction.setText("-");
                return;
            }

            double prixParNuit = h.getPrixParNuit();
            labelReduction.setText("-");

            if (Objects.equals(Inscription.getUtilisateurConnecte().getTypeUtilisateur(), "ancien")) {
                Reduction reduction = hebergementDAO.getReductionParHebergement(h.getId());
                if (reduction != null) {
                    prixParNuit = prixParNuit * (1 - reduction.getPourcentage() / 100.0);
                    labelReduction.setForeground(new Color(0, 128, 0));
                    labelReduction.setText(reduction.getPourcentage() + "% de réduction appliquée");
                } else {
                    labelReduction.setText("-");
                }
            }

            champPrixParNuit.setText(String.format(Locale.US, "%.2f", prixParNuit));

            Date dateArrivee = resetTime((Date) dateArriveeSpinner.getValue());
            Date dateDepart = resetTime((Date) dateDepartSpinner.getValue());
            int nbAdultes = (int) spinnerNbAdultes.getValue();
            int nbEnfants = (int) spinnerNbEnfants.getValue();

            if (!dateDepart.after(dateArrivee)) {
                champPrixTotal.setText("");
                labelMessage.setText("La date de départ doit être après la date d'arrivée.");
                return;
            }

            long difference = dateDepart.getTime() - dateArrivee.getTime();
            long nombreNuits = Math.max(1, difference / (1000 * 60 * 60 * 24));

            double prixTotal = prixParNuit * nombreNuits * nbAdultes
                    + (prixParNuit * 0.5 * nbEnfants * nombreNuits);

            champPrixTotal.setText(String.format(Locale.US, "%.2f", prixTotal));
            labelMessage.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            champPrixTotal.setText("");
            labelMessage.setText("Erreur lors du calcul.");
            labelReduction.setText("-");
        }
    }

    private Date resetTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getDateArrivee() { return (Date) dateArriveeSpinner.getValue(); }
    public Date getDateDepart() { return (Date) dateDepartSpinner.getValue(); }
    public int getNbAdultes() { return (int) spinnerNbAdultes.getValue(); }
    public int getNbEnfants() { return (int) spinnerNbEnfants.getValue(); }
    public double getPrixTotal() {
        try {
            return Double.parseDouble(champPrixTotal.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion prix total : " + champPrixTotal.getText());
            return 0.0;
        }
    }

    public void ajouterEcouteur(ActionListener listener) {
        btnValider.addActionListener(listener);
    }
}
