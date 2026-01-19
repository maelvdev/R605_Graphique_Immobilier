package views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import models.DonneeImmobiliere;

public class panel_graph_principal extends JPanel
{
	private List<DonneeImmobiliere> donnees;
	private String nomAxeX = "Surface";
	private String nomAxeY = "Prix";
	private boolean afficherGrille = true;

	// On stocke ces valeurs pour que le clic de souris puisse utiliser
	// la même échelle que le dessin.
	private double minX, maxX, minY, maxY;
	private int padding = 60; // Marge un peu plus grande pour afficher le texte

	public panel_graph_principal()
	{
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		// --- GESTION DU CLIC ---
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				detecterClicSurPoint(e.getX(), e.getY());
			}
		});
	}

	public void setParametres(List<DonneeImmobiliere> donnees, String x, String y, boolean grille)
	{
		this.donnees = donnees;
		this.nomAxeX = x;
		this.nomAxeY = y;
		this.afficherGrille = grille;

		// On force le recalcul des min/max
		calculerMinMax();

		repaint();
	}

	// Méthode pour préparer les échelles avant de dessiner
	private void calculerMinMax()
	{
		if (donnees == null || donnees.isEmpty())
			return;

		minX = Double.MAX_VALUE;
		maxX = Double.MIN_VALUE;
		minY = Double.MAX_VALUE;
		maxY = Double.MIN_VALUE;

		for (DonneeImmobiliere d : donnees)
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

		// Ajouter une marge de 10% pour que les points ne touchent pas les
		// bords
		double margeX = (maxX - minX) * 0.1;
		double margeY = (maxY - minY) * 0.1;

		// Si toutes les valeurs sont identiques, on force une marge
		if (margeX == 0)
			margeX = 10;
		if (margeY == 0)
			margeY = 10;

		maxX += margeX;
		minX = Math.max(0, minX - margeX); // On évite les valeurs négatives si
											// possible

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

		// Calcul des échelles de conversion
		double echelleX = graphWidth / (maxX - minX);
		double echelleY = graphHeight / (maxY - minY);

		// 1. Dessiner la Grille ET les Graduations
		g2.setFont(new Font("Arial", Font.PLAIN, 10));
		int nbDivisions = 5; // Nombre de lignes de grille

		for (int i = 0; i <= nbDivisions; i++)
		{
			// Position relative (de 0.0 à 1.0)
			double ratio = (double) i / nbDivisions;

			// --- AXE Y (Horizontal lines) ---
			int yPos = height - padding - (int) (ratio * graphHeight);
			double valY = minY + ratio * (maxY - minY);

			if (afficherGrille)
			{
				g2.setColor(new Color(220, 220, 220));
				g2.drawLine(padding, yPos, width - padding, yPos);
			}
			// Texte Axe Y (Prix, etc.)
			g2.setColor(Color.BLACK);
			String txtY = formatNombre(valY);
			// On aligne le texte à droite de l'axe Y
			int txtWidth = g2.getFontMetrics().stringWidth(txtY);
			g2.drawString(txtY, padding - txtWidth - 5, yPos + 4);

			// --- AXE X (Vertical lines) ---
			int xPos = padding + (int) (ratio * graphWidth);
			double valX = minX + ratio * (maxX - minX);

			if (afficherGrille)
			{
				g2.setColor(new Color(220, 220, 220));
				g2.drawLine(xPos, padding, xPos, height - padding);
			}
			// Texte Axe X
			g2.setColor(Color.BLACK);
			String txtX = formatNombre(valX);
			// On centre le texte sous la ligne
			int txtWidthX = g2.getFontMetrics().stringWidth(txtX);
			g2.drawString(txtX, xPos - (txtWidthX / 2), height - padding + 15);
		}

		// 2. Dessiner les axes principaux (Noir) par dessus la grille
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2)); // Ligne un peu plus épaisse
		g2.drawLine(padding, height - padding, width - padding, height - padding); // X
		g2.drawLine(padding, height - padding, padding, padding); // Y
		g2.setStroke(new BasicStroke(1)); // Reset épaisseur

		// Titres des axes
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawString(nomAxeX, width - padding - 20, height - padding + 35);
		g2.drawString(nomAxeY, padding - 20, padding - 10);

		// 3. Dessiner les points
		g2.setColor(Color.BLUE);
		for (DonneeImmobiliere d : donnees)
		{
			Point p = convertirDonneeEnPixel(d, width, height);
			g2.fillOval(p.x - 4, p.y - 4, 8, 8); // Points un peu plus gros
													// (8px)
		}
	}

	/**
	 * Convertit une donnée immobilière en coordonnées (x, y) pixels sur l'écran
	 */
	private Point convertirDonneeEnPixel(DonneeImmobiliere d, int width, int height)
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

	/**
	 * Détecte si un clic est proche d'un point et affiche les infos
	 */
	private void detecterClicSurPoint(int mouseX, int mouseY)
	{
		if (donnees == null)
			return;

		int tolerance = 8; // Rayon de tolérance en pixels pour le clic (clic
							// facile)

		for (DonneeImmobiliere d : donnees)
		{
			// On recalcule la position actuelle du point
			Point p = convertirDonneeEnPixel(d, getWidth(), getHeight());

			// Calcul de la distance entre la souris et le point (Pythagore)
			double distance = Math.sqrt(Math.pow(mouseX - p.x, 2) + Math.pow(mouseY - p.y, 2));

			if (distance <= tolerance)
			{
				// Bingo ! On a cliqué sur ce point
				afficherInfoBulle(d);
				return; // On arrête après avoir trouvé le premier point
			}
		}
	}

	private void afficherInfoBulle(DonneeImmobiliere d)
	{
		String message = String.format(
				"Détails du bien :\n\n" + "Année : %d\n" + "Prix : %.2f €\n" + "Surface : %.2f m²\n" + "Pièces : %d",
				d.getAnnee(), d.getPrix(), d.getSurface(), d.getNbPieces());

		JOptionPane.showMessageDialog(this, message, "Information Point", JOptionPane.INFORMATION_MESSAGE);
	}

	// Utilitaire pour afficher des nombres propres (ex: 150000 au lieu de
	// 150000.0)
	private String formatNombre(double val)
	{
		if (val >= 10000)
		{
			return String.format("%.0f", val); // Pas de virgule pour les gros
												// chiffres
		}
		else
		{
			return String.format("%.1f", val); // 1 chiffre après la virgule
												// pour les petits
		}
	}
}