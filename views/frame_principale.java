package views;

import javax.swing.*;

import graphique_immobilier.Controlleur;

import java.awt.*;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

import models.Ville;

public class frame_principale extends JFrame
{

	// Composants
	private panel_parametres_top panelTop;
	private panel_graph_principal panelGraph;
	private panel_tab_donnees panelTab;
	private JSplitPane splitPane;

	// Données
	private List<Ville> donnees;

	public frame_principale()
	{
		// --- Configuration de la fenêtre ---
		setTitle("Application Immobilière");
		setSize(1100, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centrer à l'écran
		setLayout(new BorderLayout());

		// --- Initialisation des données ---
		donnees = chargerDonnees();

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
			// On recharge les données SQL et on met à jour tout le monde
			donnees = chargerDonnees();
			panelTab.mettreAJourDonnees(donnees);
			actualiserGraphique();
		});
	}

	private List<Ville> chargerDonnees()
	{
		try
		{
			Path sqlPath = Paths.get("data_compressor", "test_dataset.sql");
			List<Ville> villes = Controlleur.chargerVillesDepuisSql(sqlPath);
			if (villes == null || villes.isEmpty())
			{
				JOptionPane.showMessageDialog(this,
						"Aucune donnée trouvée dans test_dataset.sql (INSERT INTO Villes).",
						"Données manquantes", JOptionPane.WARNING_MESSAGE);
			}
			return villes;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"Impossible de charger data_compressor/test_dataset.sql : " + ex.getMessage(),
					"Erreur de chargement", JOptionPane.ERROR_MESSAGE);
			return java.util.List.of();
		}
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