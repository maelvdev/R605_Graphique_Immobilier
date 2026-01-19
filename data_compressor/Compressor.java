import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Compressor {

	private ArrayList<HashMap<String, String>> dataCodeCommuneNomVilleEtPopulation;

	private ArrayList<HashMap<String, String>> dataCodeCommune;

	private HashMap<String, String> dataVilles;
	private HashMap<String, String> dataPrix;
	private HashMap<String, String> dataSurface;


	public Compressor() {
		this.dataCodeCommuneNomVilleEtPopulation = Reader.lireCSV(3, new int[]{4,8}, ';', "files/communes_littorales_2019.csv");

		ArrayList<HashMap<String, String>> resultats = Reader.lireCSV(0, new int[]{10, 3, 31}, '|', "files/dvf_data.txt");
		
		this.dataVilles = resultats.get(0);
		this.dataPrix = resultats.get(1);
		this.dataSurface = resultats.get(2);
		System.out.println("données :" + resultats.size());

		calculerPrixMoyenParVille();

		//System.out.println(this.dataCodeCommuneNomVilleEtPopulation);
		System.out.println(this.dataPrix);
		System.out.println(this.dataSurface);

		System.out.println("Nombre de ventes avant tri : " + this.dataVilles.size());
		System.out.println("Nombre de villes littorales : " + this.dataCodeCommuneNomVilleEtPopulation.get(0).size());

	}

	private void calculerPrixMoyenParVille() {
		HashMap<String, Double> sommePrixM2ParVille = new HashMap<>();
		HashMap<String, Integer> nombreVentesParVille = new HashMap<>();

		for (String idVente : dataVilles.keySet()) {
			
			String ville = dataVilles.get(idVente);
			String strPrix = dataPrix.get(idVente);
			String strSurface = dataSurface.get(idVente);

			try {
				if (strPrix == null || strPrix.isEmpty() || strSurface == null || strSurface.isEmpty()) continue;

				double prix = Double.parseDouble(strPrix.replace(",", "."));
				double surface = Double.parseDouble(strSurface.replace(",", "."));

				if (surface > 0 && prix > 0) {
					
					double prixAuM2 = prix / surface;

					sommePrixM2ParVille.put(ville, sommePrixM2ParVille.getOrDefault(ville, 0.0) + prixAuM2);

					nombreVentesParVille.put(ville, nombreVentesParVille.getOrDefault(ville, 0) + 1);
				}

			} catch (NumberFormatException e) {
			}
		}

		// affichage ici pour l'instant
		System.out.println("--- PRIX MOYEN AU M² PAR VILLE ---");
		for (String ville : sommePrixM2ParVille.keySet()) {
			double totalPrixM2 = sommePrixM2ParVille.get(ville);
			int nombreVentes = nombreVentesParVille.get(ville);
			
			double moyenne = totalPrixM2 / nombreVentes;
			
			System.out.println("Ville : " + ville + " -> " + String.format("%.2f", moyenne) + " €/m² (" + nombreVentes + " ventes)");
		}
	}

	private void trierDonneesParVilles(ArrayList<HashMap<String, String>> donnesATrier, ArrayList<HashMap<String, String>> donnesDeTri) {
		Set<String> clesValides = new HashSet<>();

		for (HashMap<String, String> mapTri : donnesDeTri) {
			clesValides.addAll(mapTri.keySet());
		}

		for (HashMap<String, String> mapATrier : donnesATrier) {
			mapATrier.keySet().retainAll(clesValides);
		}
	}

	public static void main(String[] args)
	{
		Compressor c = new Compressor();
	}
}
