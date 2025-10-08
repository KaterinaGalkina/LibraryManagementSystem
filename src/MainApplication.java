import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainApplication {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:database/library_info.db"; 

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connected to SQLite database!");

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM people;");

                System.out.println("People:");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " | " +
                                       rs.getString("first_name") + " " +
                                       rs.getString("last_name")  + " " +
                                       rs.getString("birth_date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
