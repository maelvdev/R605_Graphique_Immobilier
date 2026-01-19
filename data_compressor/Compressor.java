import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Compressor {

	private ArrayList<HashMap<String, String>> dataCodeCommuneNomVilleEtPopulation;

	private ArrayList<HashMap<String, String>> dataCodeCommune;


	public Compressor() {
		this.dataCodeCommuneNomVilleEtPopulation = Reader.lireCSV(3, new int[]{4,8}, ';', "files/communes_littorales_2019.csv");

		System.out.println(this.dataCodeCommuneNomVilleEtPopulation);

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
