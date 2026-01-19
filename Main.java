import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import views.panel_principal;

public class Main
{
	public static void main(String[] args)
	{
		// Swing n'est pas "Thread Safe", il faut toujours lancer l'interface
		// dans l'Event Dispatch Thread (EDT) via invokeLater.
		SwingUtilities.invokeLater(() -> {
			try
			{
				// Tente d'appliquer le style visuel du système (Windows, Mac,
				// etc.)
				// au lieu du style Java "Metal" par défaut.
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e)
			{
				// Si ça échoue, on garde le style par défaut, ce n'est pas
				// grave.
				e.printStackTrace();
			}

			// Création de la fenêtre principale
			JFrame frame = new JFrame("Visualisation Données Immobilières");

			// Fermer l'application quand on clique sur la croix
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Taille de départ (Largeur, Hauteur)
			frame.setSize(1100, 750);

			// Centrer la fenêtre sur l'écran
			frame.setLocationRelativeTo(null);

			// Ajout de votre panneau principal qui contient tout le reste
			// Note: Assurez-vous que panel_principal est bien importé ci-dessus
			panel_principal mainPanel = new panel_principal();
			frame.add(mainPanel);

			// Rendre la fenêtre visible
			frame.setVisible(true);
		});
	}
}