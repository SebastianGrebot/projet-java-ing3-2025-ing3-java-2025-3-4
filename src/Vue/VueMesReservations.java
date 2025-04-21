package Vue;

import Controleur.Inscription;
import Dao.AvisDAOImpl;
import Dao.DaoFactory;
import Dao.HebergementDAOImpl;
import Dao.ReservationDAOImpl;
import Modele.Avis;
import Modele.Hebergement;
import Modele.Reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;

public class VueMesReservations extends JFrame {
    private JPanel panelPrincipal;
    private ReservationDAOImpl reservationDAO;
    private HebergementDAOImpl hebergementDAO;
    private AvisDAOImpl avisDAO;

    public VueMesReservations(ReservationDAOImpl reservationDAO, HebergementDAOImpl hebergementDAO, AvisDAOImpl avisDAO) {
        this.reservationDAO = reservationDAO;
        this.hebergementDAO = hebergementDAO;
        this.avisDAO = avisDAO;

        setTitle("Mes Réservations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);

        afficherReservationsUtilisateur();

        add(scrollPane);
    }

    private void afficherReservationsUtilisateur() {
        int utilisateurId = Inscription.getUtilisateurId();
        ArrayList<Reservation> toutesLesReservations = reservationDAO.getAll();

        for (Reservation res : toutesLesReservations) {
            if (res.getUtilisateurId() == utilisateurId) {
                JPanel panelResa = new JPanel();
                panelResa.setLayout(new BoxLayout(panelResa, BoxLayout.Y_AXIS));
                panelResa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panelResa.setBackground(new Color(245, 250, 255));

                Hebergement hebergement = hebergementDAO.chercher(res.getHebergementId());
                String nomHebergement = (hebergement != null) ? hebergement.getNom() : "Hébergement inconnu";

                JLabel titre = new JLabel("🏡 " + nomHebergement);
                titre.setFont(new Font("Arial", Font.BOLD, 16));
                panelResa.add(titre);

                JTextArea infos = new JTextArea();
                infos.setEditable(false);
                infos.setBackground(new Color(245, 250, 255));
                infos.setFont(new Font("SansSerif", Font.PLAIN, 13));
                infos.setText(
                        "Date d'arrivée : " + res.getDateArrivee() +
                                "\nDate de départ : " + res.getDateDepart() +
                                "\nAdultes : " + res.getAdultes() +
                                "\nEnfants : " + res.getEnfants() +
                                "\nPrix total : " + res.getPrixTotal() + " €" +
                                "\nStatut : " + res.getStatut()
                );
                panelResa.add(infos);

                // Panel avis
                JPanel panelAvisNote = new JPanel(new BorderLayout());
                panelAvisNote.setBorder(BorderFactory.createTitledBorder("Votre avis"));
                panelAvisNote.setBackground(new Color(255, 255, 255));

                JTextArea champAvis = new JTextArea(3, 30);
                champAvis.setLineWrap(true);
                champAvis.setWrapStyleWord(true);
                JScrollPane scrollAvis = new JScrollPane(champAvis);
                panelAvisNote.add(scrollAvis, BorderLayout.CENTER);

                // Panel note
                JPanel panelNote = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel labelNote = new JLabel("Note : ");
                JComboBox<Integer> comboNote = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
                panelNote.add(labelNote);
                panelNote.add(comboNote);
                panelAvisNote.add(panelNote, BorderLayout.NORTH);

                // Bouton pour envoyer l'avis
                JButton boutonAvis = new JButton("Entrer l'avis");
                boutonAvis.setBackground(new Color(70, 130, 180));
                boutonAvis.setForeground(Color.WHITE);

                boutonAvis.addActionListener(e -> {
                    int note = (int) comboNote.getSelectedItem();
                    String commentaire = champAvis.getText().trim();

                    if (commentaire.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Veuillez entrer un commentaire.");
                        return;
                    }

                    Avis avis = new Avis(
                            Inscription.getUtilisateurId(),
                            res.getHebergementId(),
                            note,
                            commentaire,
                            new Date(System.currentTimeMillis())
                    );

                    avisDAO.ajouterAvis(avis);

                    JOptionPane.showMessageDialog(this, "Avis enregistré avec succès !");
                    boutonAvis.setEnabled(false);
                    champAvis.setEditable(false);
                    comboNote.setEnabled(false);
                });

                panelAvisNote.add(boutonAvis, BorderLayout.SOUTH);

                panelResa.add(panelAvisNote);
                panelResa.setAlignmentX(Component.LEFT_ALIGNMENT);

                panelPrincipal.add(panelResa);
                panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15))); // espace entre les réservations
            }
        }
    }
}
