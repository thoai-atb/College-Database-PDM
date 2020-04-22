package general;
import java.sql.*;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException {
		String url = "jdbc:sqlserver://DESKTOP-5CTMAPF\\HOHNER:63849;databaseName=CollegeDatabase;user=sa;password=password;";
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection(url);
			System.out.println("Connected");
			new LoginInterface(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
