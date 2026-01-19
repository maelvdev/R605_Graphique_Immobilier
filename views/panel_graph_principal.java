package views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import models.Ville; // <-- Correction de l'import (C'était DonneeImmobiliere avant)

public class panel_graph_principal extends JPanel
{
	private List<Ville> donnees; // <-- Utilisation de Ville
	private String nomAxeX = "Population";
	private String nomAxeY = "Prix m²";
	private boolean afficherGrille = true;

	private double minX, maxX, minY, maxY;
	private int padding = 60;

	public panel_graph_principal()
	{
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				detecterClicSurPoint(e.getX(), e.getY());
			}
		});
	}

	public void setParametres(List<Ville> donnees, String x, String y, boolean grille)
	{
		this.donnees = donnees;
		this.nomAxeX = x;
		this.nomAxeY = y;
		this.afficherGrille = grille;

		calculerMinMax();
		repaint();
	}

	private void calculerMinMax()
	{
		if (donnees == null || donnees.isEmpty())
			return;

		minX = Double.MAX_VALUE;
		maxX = Double.MIN_VALUE;
		minY = Double.MAX_VALUE;
		maxY = Double.MIN_VALUE;

		for (Ville d : donnees) // <-- Boucle sur Ville
		{
			double valX = d.getValeurParNom(nomAxeX);
			double valY = d.getValeurParNom(nomAxeY);
			if (valX < minX)
				minX = valX;
			if (valX > maxX)
				maxX = valX;
			if (valY < minY)
				minY = valY;
			if (valY > maxY)
				maxY = valY;
		}

		double margeX = (maxX - minX) * 0.1;
		double margeY = (maxY - minY) * 0.1;

		if (margeX == 0)
			margeX = 10;
		if (margeY == 0)
			margeY = 10;

		maxX += margeX;
		minX = Math.max(0, minX - margeX);
		maxY += margeY;
		minY = Math.max(0, minY - margeY);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (donnees == null || donnees.isEmpty())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();
		int graphWidth = width - 2 * padding;
		int graphHeight = height - 2 * padding;

		// 1. Dessin de la Grille
		g2.setFont(new Font("Arial", Font.PLAIN, 10));
		int nbDivisions = 5;

		for (int i = 0; i <= nbDivisions; i++)
		{
			double ratio = (double) i / nbDivisions;
			int yPos = height - padding - (int) (ratio * graphHeight);
			double valY = minY + ratio * (maxY - minY);

			if (afficherGrille)
			{
				g2.setColor(new Color(220, 220, 220));
				g2.drawLine(padding, yPos, width - padding, yPos);
			}
			g2.setColor(Color.BLACK);
			String txtY = formatNombre(valY);
			int txtWidth = g2.getFontMetrics().stringWidth(txtY);
			g2.drawString(txtY, padding - txtWidth - 5, yPos + 4);

			int xPos = padding + (int) (ratio * graphWidth);
			double valX = minX + ratio * (maxX - minX);

			if (afficherGrille)
			{
				g2.setColor(new Color(220, 220, 220));
				g2.drawLine(xPos, padding, xPos, height - padding);
			}
			g2.setColor(Color.BLACK);
			String txtX = formatNombre(valX);
			int txtWidthX = g2.getFontMetrics().stringWidth(txtX);
			g2.drawString(txtX, xPos - (txtWidthX / 2), height - padding + 15);
		}

		// 2. Axes
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(padding, height - padding, width - padding, height - padding);
		g2.drawLine(padding, height - padding, padding, padding);
		g2.setStroke(new BasicStroke(1));

		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawString(nomAxeX, width - padding - 20, height - padding + 35);
		g2.drawString(nomAxeY, padding - 20, padding - 10);

		// 3. Points
		g2.setColor(Color.BLUE);
		for (Ville d : donnees) // <-- Boucle sur Ville
		{
			Point p = convertirDonneeEnPixel(d, width, height);
			g2.fillOval(p.x - 4, p.y - 4, 8, 8);
		}
	}

	// <-- Paramètre changé en Ville
	private Point convertirDonneeEnPixel(Ville d, int width, int height)
	{
		double valX = d.getValeurParNom(nomAxeX);
		double valY = d.getValeurParNom(nomAxeY);

		int graphWidth = width - 2 * padding;
		int graphHeight = height - 2 * padding;

		double echelleX = graphWidth / (maxX - minX);
		double echelleY = graphHeight / (maxY - minY);

		int x = padding + (int) ((valX - minX) * echelleX);
		int y = height - padding - (int) ((valY - minY) * echelleY);

		return new Point(x, y);
	}

	private void detecterClicSurPoint(int mouseX, int mouseY)
	{
		if (donnees == null)
			return;
		int tolerance = 8;

		for (Ville d : donnees) // <-- Boucle sur Ville
		{
			Point p = convertirDonneeEnPixel(d, getWidth(), getHeight());
			double distance = Math.sqrt(Math.pow(mouseX - p.x, 2) + Math.pow(mouseY - p.y, 2));

			if (distance <= tolerance)
			{
				afficherInfoBulle(d);
				return;
			}
		}
	}

	// <-- Mise à jour de l'affichage pour correspondre aux attributs de Ville
	private void afficherInfoBulle(Ville d)
	{
		String message = String.format(
				"Détails de la ville :\n\n" + "Nom : %s (Code: %d)\n" + "Prix m² : %.0f €\n" + "Population : %d hab\n"
						+ "Densité : %.1f hab/km²\n" + "Chômage : %.1f %%",
				d.getNom(), d.getCode(), d.getPrixM2(), d.getPopulation(), d.getDensite(), d.getChomage());

		JOptionPane.showMessageDialog(this, message, "Info Ville", JOptionPane.INFORMATION_MESSAGE);
	}

	private String formatNombre(double val)
	{
		if (val >= 10000)
			return String.format("%.0f", val);
		else
			return String.format("%.1f", val);
	}
}