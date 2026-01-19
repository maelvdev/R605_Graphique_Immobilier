package utils; // Si vous voulez le mettre dans un package utilitaire

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CustomWriter
{
	// J'ai changé HashMap<?,?> en Map<?,?> pour plus de flexibilité
	public void writeMapToFile(Map<?, ?> map, String filePath) throws IOException
	{
		// Vérification de sécurité
		if (map == null || map.isEmpty())
		{
			System.out.println("La map est vide ou nulle, rien à écrire.");
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
		{
			for (Map.Entry<?, ?> entry : map.entrySet())
			{
				// On s'assure que les clés/valeurs ne sont pas nulles pour
				// éviter un crash
				String key = (entry.getKey() != null) ? entry.getKey().toString() : "null";
				String value = (entry.getValue() != null) ? entry.getValue().toString() : "null";

				writer.write(key + " = " + value);
				writer.newLine();
			}
		}
	}
}