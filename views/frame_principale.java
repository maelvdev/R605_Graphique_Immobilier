package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.DonneeImmobiliere;
import models.GenerateurDonnees;

// Cette classe est maintenant la FENÊTRE (JFrame) et non plus un panneau
public class frame_principale extends JFrame
{

	// Composants
	private panel_parametres_top panelTop;
	private panel_graph_principal panelGraph;
	private panel_tab_donnees panelTab;
	private JSplitPane splitPane;

	// Données
	private List<DonneeImmobiliere> donnees;

	public frame_principale()
	{
		// --- Configuration de la fenêtre ---
		setTitle("Application Immobilière");
		setSize(1100, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centrer à l'écran
		setLayout(new BorderLayout());

		// --- Initialisation des données ---
		donnees = GenerateurDonnees.genererDonneesAleatoires(50);

		// --- Création des vues (JPanels) ---
		panelTop = new panel_parametres_top();
		panelGraph = new panel_graph_principal();
		panelTab = new panel_tab_donnees();

		// --- Configuration initiale ---
		panelTab.mettreAJourDonnees(donnees);
		actualiserGraphique();

		// --- Organisation de l'affichage (SplitPane) ---
		// Graphique en haut, Tableau en bas
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelGraph, panelTab);
		splitPane.setResizeWeight(0.6); // Le graph prend 60% de la hauteur
		splitPane.setDividerLocation(400);

		// --- Ajout des éléments à la fenêtre ---
		// "add" ici ajoute directement au contenu de la JFrame
		add(panelTop, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);

		// --- Gestion des événements (Boutons) ---

		// Clic sur "Appliquer"
		panelTop.getBtnAppliquer().addActionListener(e -> {
			actualiserGraphique();
		});

		// Clic sur "Réinitialiser"
		panelTop.getBtnReinitialiser().addActionListener(e -> {
			// On régénère des données et on met à jour tout le monde
			donnees = GenerateurDonnees.genererDonneesAleatoires(50);
			panelTab.mettreAJourDonnees(donnees);
			actualiserGraphique();
		});
	}

	// Méthode pour dire au graph de se redessiner selon les combobox
	private void actualiserGraphique()
	{
		String xField = panelTop.getAbscisse();
		String yField = panelTop.getOrdonnee();
		boolean grille = panelTop.isGridVisible();

		panelGraph.setParametres(donnees, xField, yField, grille);
	}

	// --- MAIN ---
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			// Créer et afficher la fenêtre
			new frame_principale().setVisible(true);
		});
	}
}