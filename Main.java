import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import views.frame_principale; // On importe la nouvelle classe

public class Main
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try
			{
				// Style visuel du système
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			// frame_principale est déjà une JFrame (une fenêtre),
			// on a juste besoin de la créer et de l'afficher.
			// On ne crée plus de "new JFrame()" ici car frame_principale EN EST
			// UNE.
			new frame_principale().setVisible(true);
		});
	}
}