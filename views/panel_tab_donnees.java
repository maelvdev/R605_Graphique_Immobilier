package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import models.Ville;

public class panel_tab_donnees extends JPanel
{
	private JTable table;
	private DefaultTableModel model;

	public panel_tab_donnees()
	{
		setLayout(new BorderLayout());

		// Définition des colonnes
		String[] colonnes = { "Code", "Nom", "Littoral", "Montagnes", "Population", "Densité", "Âge moyen", "Chômage",
				"Prix m²" };
		model = new DefaultTableModel(colonnes, 0);

		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		add(scrollPane, BorderLayout.CENTER);
	}

	public void mettreAJourDonnees(List<Ville> donnees)
	{
		model.setRowCount(0); // Vider le tableau
		for (Ville v : donnees)
		{
			model.addRow(new Object[] { v.getCode(), v.getNom(), v.isLittoral(), v.isMontagnes(), v.getPopulation(),
					String.format("%.2f", v.getDensite()), v.getAgeMoyen(), String.format("%.1f", v.getChomage()),
					String.format("%.0f", v.getPrixM2()) });
		}
	}
}