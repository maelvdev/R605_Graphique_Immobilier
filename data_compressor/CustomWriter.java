import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomWriter {

	public static void saveListToDatabase(ArrayList<VilleResultat> listData) {
		String host = "aws-1-eu-west-1.pooler.supabase.com";
		String database = "postgres";
		String user = "postgres.inzduywgrvsaudlggsfb";
		String password = "devAvancé12";
		String url = "jdbc:postgresql://" + host + ":6543/" + database + "?sslmode=require";

		String sql = "INSERT INTO Ville (code, nom, prix_m2) VALUES (?, ?, ?) " +
				"ON CONFLICT (code) DO UPDATE SET prix_m2 = EXCLUDED.prix_m2, nom = EXCLUDED.nom";

		try { Class.forName("org.postgresql.Driver"); } catch (Exception e) {}

		try (Connection conn = DriverManager.getConnection(url, user, password);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			conn.setAutoCommit(false);

			int count = 0;
			int batchSize = 1000;

			System.out.println("Début de l'insertion de " + listData.size() + " lignes...");

			for (VilleResultat ville : listData) {

				pstmt.setString(1, ville.code);
				pstmt.setString(2, ville.nom);
				pstmt.setDouble(3, ville.prixMoyen);

				pstmt.addBatch();
				count++;

				if (count % batchSize == 0) {
					pstmt.executeBatch();
					conn.commit();
					System.out.println(count + " lignes insérées...");
				}
			}

			pstmt.executeBatch();
			conn.commit();

			System.out.println("Terminé ! Total : " + count + " lignes.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}