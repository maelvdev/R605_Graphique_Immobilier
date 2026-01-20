package graphique_immobilier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import models.Ville;

public class Controlleur
{
	private Controlleur()
	{
		// Classe utilitaire
	}

	public static List<Ville> chargerVillesDepuisSql(Path sqlPath) throws IOException
	{
		String contenu = Files.readString(sqlPath, StandardCharsets.UTF_8);

		int insertIndex = indexOfIgnoreCase(contenu, "insert into villes");
		if (insertIndex < 0)
		{
			return List.of();
		}

		int valuesIndex = indexOfIgnoreCase(contenu, "values", insertIndex);
		if (valuesIndex < 0)
		{
			return List.of();
		}

		int start = contenu.indexOf('(', valuesIndex);
		int end = contenu.indexOf(';', valuesIndex);
		if (start < 0 || end < 0 || end <= start)
		{
			return List.of();
		}

		String blocValues = contenu.substring(start, end);
		List<String> tuples = extraireTuples(blocValues);

		List<Ville> villes = new ArrayList<>();
		for (String tuple : tuples)
		{
			List<String> champs = splitCsvSqlTuple(tuple);
			if (champs.size() < 9)
			{
				continue;
			}

			int code = parseInt(champs.get(0));
			String nom = parseSqlString(champs.get(1));
			boolean littoral = parseBoolean(champs.get(2));
			boolean montagnes = parseBoolean(champs.get(3));
			int population = parseInt(champs.get(4));
			double densite = parseDouble(champs.get(5));
			int ageMoyen = parseInt(champs.get(6));
			double chomage = parseDouble(champs.get(7));
			double prixM2 = parseDouble(champs.get(8));

			villes.add(new Ville(code, nom, littoral, montagnes, population, densite, ageMoyen, chomage, prixM2));
		}

		return villes;
	}

	private static int indexOfIgnoreCase(String texte, String motif)
	{
		return indexOfIgnoreCase(texte, motif, 0);
	}

	private static int indexOfIgnoreCase(String texte, String motif, int aPartirDe)
	{
		return texte.toLowerCase().indexOf(motif.toLowerCase(), aPartirDe);
	}

	private static List<String> extraireTuples(String bloc)
	{
		List<String> tuples = new ArrayList<>();
		boolean dansGuillemets = false;
		int profondeur = 0;
		int debutTuple = -1;

		for (int i = 0; i < bloc.length(); i++)
		{
			char c = bloc.charAt(i);
			if (c == '\'')
			{
				if (dansGuillemets && i + 1 < bloc.length() && bloc.charAt(i + 1) == '\'')
				{
					i++;
					continue;
				}
				dansGuillemets = !dansGuillemets;
				continue;
			}

			if (dansGuillemets)
				continue;

			if (c == '(')
			{
				if (profondeur == 0)
				{
					debutTuple = i + 1;
				}
				profondeur++;
			}
			else if (c == ')')
			{
				profondeur--;
				if (profondeur == 0 && debutTuple >= 0)
				{
					tuples.add(bloc.substring(debutTuple, i));
					debutTuple = -1;
				}
			}
		}

		return tuples;
	}

	private static List<String> splitCsvSqlTuple(String tupleSansParens)
	{
		List<String> resultat = new ArrayList<>();
		StringBuilder courant = new StringBuilder();
		boolean dansGuillemets = false;

		for (int i = 0; i < tupleSansParens.length(); i++)
		{
			char c = tupleSansParens.charAt(i);
			if (c == '\'')
			{
				if (dansGuillemets && i + 1 < tupleSansParens.length() && tupleSansParens.charAt(i + 1) == '\'')
				{
					courant.append('\'');
					i++;
					continue;
				}
				dansGuillemets = !dansGuillemets;
				courant.append(c);
				continue;
			}

			if (c == ',' && !dansGuillemets)
			{
				resultat.add(courant.toString().trim());
				courant.setLength(0);
				continue;
			}

			courant.append(c);
		}

		String dernier = courant.toString().trim();
		if (!dernier.isEmpty())
		{
			resultat.add(dernier);
		}

		return resultat;
	}

	private static String parseSqlString(String brut)
	{
		String s = brut.trim();
		if (s.startsWith("'") && s.endsWith("'") && s.length() >= 2)
		{
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

	private static boolean parseBoolean(String brut)
	{
		return Boolean.parseBoolean(brut.trim());
	}

	private static int parseInt(String brut)
	{
		return Integer.parseInt(brut.trim());
	}

	private static double parseDouble(String brut)
	{
		return Double.parseDouble(brut.trim());
	}
}
