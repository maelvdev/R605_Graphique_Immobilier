import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class Compressor {

	private ArrayList<HashMap<String, String>> dataCodeCommuneNomVilleEtPopulation;

	public Compressor() {
		System.out.println("Chargement des communes littorales...");
		this.dataCodeCommuneNomVilleEtPopulation = Reader.lireCSV(3, new int[]{4, 8}, ';', "files/communes_littorales_2019.csv");

		traiterDvfEtCalculerMoyenne("files/valeurfonciere2024.txt");
	}

	private void traiterDvfEtCalculerMoyenne(String cheminDvf) {
		HashMap<String, Double> sommePrixM2ParCode = new HashMap<>();
		HashMap<String, Integer> nombreVentesParCode = new HashMap<>();
		ArrayList<VilleResultat> listeAInserer = new ArrayList<>();

		Set<String> codesLittoraux = dataCodeCommuneNomVilleEtPopulation.get(0).keySet();
		System.out.println("Filtre actif sur " + codesLittoraux.size() + " communes littorales.");

		// INDICES DVF (Basé sur votre header fourni)
		int COL_NATURE = 9;   // Nature mutation (Vente)
		int COL_VALEUR = 10;  // Valeur foncière
		int COL_COMMUNE = 17; // Nom de la commune
		int COL_DEPT = 18;    // Code département (ex: 01)
		int COL_CODE_COM = 19;// Code commune (ex: 76)
		int COL_SURFACE = 38; // Surface reelle bati

		try (BufferedReader br = new BufferedReader(new FileReader(cheminDvf))) {
			String line;
			int countLignes = 0;

			while ((line = br.readLine()) != null) {
				countLignes++;

				String[] row = line.split(Pattern.quote("|"), -1);

				if (row.length <= COL_SURFACE) continue;

				if (!row[COL_NATURE].equals("Vente")) continue;

				String dept = row[COL_DEPT];
				String com = row[COL_CODE_COM];

				String codeInsee = genererCodeInsee(dept, com);

				if (!codesLittoraux.contains(codeInsee)) {
					continue;
				}

				String strPrix = row[COL_VALEUR];
				String strSurface = row[COL_SURFACE];

				try {
					if (strPrix.isEmpty() || strSurface.isEmpty()) continue;

					double prix = Double.parseDouble(strPrix.replace(",", "."));
					double surface = Double.parseDouble(strSurface.replace(",", "."));

					if (surface > 0 && prix > 1000) {
						double prixAuM2 = prix / surface;

						sommePrixM2ParCode.put(codeInsee, sommePrixM2ParCode.getOrDefault(codeInsee, 0.0) + prixAuM2);
						nombreVentesParCode.put(codeInsee, nombreVentesParCode.getOrDefault(codeInsee, 0) + 1);
					}
				} catch (NumberFormatException e) {
				}
			}
			System.out.println("Lecture DVF terminée. " + countLignes + " lignes analysées.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("--- PRIX MOYEN AU M² (LITTORAL) ---");

		for (String codeInsee : sommePrixM2ParCode.keySet()) {
			double totalPrixM2 = sommePrixM2ParCode.get(codeInsee);
			int nombreVentes = nombreVentesParCode.get(codeInsee);
			double moyenne = totalPrixM2 / nombreVentes;

			String nomVille = dataCodeCommuneNomVilleEtPopulation.get(0).get(codeInsee);

			listeAInserer.add(new VilleResultat(codeInsee, nomVille, moyenne));
		}

		System.out.println("Nombre de communes prêtes : " + listeAInserer.size());
		CustomWriter.saveListToDatabase(listeAInserer);
	}


	private String genererCodeInsee(String dept, String commune) {
		if (dept == null || commune == null) return "";

		// Nettoyage
		dept = dept.trim();
		commune = commune.trim();

		// Cas classique : Dept (2 chars) + Commune (3 chars)
		// Si la commune est "76", il faut la transformer en "076"
		if (commune.length() == 1) commune = "00" + commune;
		else if (commune.length() == 2) commune = "0" + commune;

		// Cas DOM-TOM (Dept 3 chars ex: 974)
		return dept + commune;
	}

	public static void main(String[] args) {
		new Compressor();
	}
}