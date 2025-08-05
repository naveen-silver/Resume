import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/MEC";
    private static final String USER = "root";
    private static final String PASS = "root";
    public static void main(String[] args) {
        String insertSql = "INSERT INTO employee (name, salary) VALUES (?, ?)";
        String selectSql = "SELECT id,name,salary FROM employee";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                System.out.println("---Excuting Insert---");
                insertStmt.setString(1, "John Doe");
                insertStmt.setDouble(2, 50000.00);
                int rowsAffected = insertStmt.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully ");

                insertStmt.setString(1, "Jane Smith");
                insertStmt.setDouble(2, 65000.00);   
                rowsAffected = insertStmt.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully ");    
                System.out.println("---Excuting Select---");
                insertStmt.close();
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                 ResultSet rs = selectStmt.executeQuery()) {
                    System.out.println("Employees data:");
                    System.out.println("----------------------");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double salary = rs.getDouble("salary");
                    System.out.println("ID: " + id + ", Name: " + name + ", Salary: " + salary);

                }
            }   
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}