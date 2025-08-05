import java.sql.*;
import java.util.*;
public class Gun  {

    String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    String DB_URL = "jdbc:mysql://localhost:3306/gun_details";
    String USER = "root";
    String PASS = "root";

    private Connection connection;
    private Scanner scanner;
    public Gun () {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully!");
            scanner = new Scanner(System.in);   
        }
        catch (SQLException se) {
            se.printStackTrace();
            System.err.println("Database connection failed. Check your DB_URL, USER, PASS, and ensure MySQL is running.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.err.println("JDBC Driver not found. Make sure mysql-connector-j-x.x.x.jar is in your classpath.");
        }
    }
        public void start() {
        if (connection == null) {
            System.err.println("Application cannot start without a database connection.");
            return;
        }
        int choice;
        do {
        displayMenu();
        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addGun();
                break;
            case 2:
                viewAllGuns();
                break;
            case 3:
                updateGun();
                break;
            case 4:
                deleteGun();
                break;
            case 5:
                System.out.println("Exiting the application.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\n-----------------------------------\n");
        } while (choice != 5);

            closeResources();
    }

    public void addGun() {
        scanner.nextLine(); 
        System.out.println("Enter Gun Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Gun Type: ");
        String type = scanner.nextLine();

        System.out.println("Enter Gun Caliber: ");
        String caliber = scanner.nextLine();

        String sql = "INSERT INTO Guns (name, type, caliber) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, caliber);
            pstmt.executeUpdate();
            System.out.println("Gun added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding Gun. Please check your input.");
        }
    }
    private void displayMenu() {
        System.out.println("--- Gun Management Application ---");
        System.out.println("1. Add Gun");
        System.out.println("2. View All Guns");
        System.out.println("3. Update Gun");
        System.out.println("4. Delete Gun");
        System.out.println("5. Exit");
        System.out.println("-----------------------------------");
    }
    private void viewAllGuns() {
        String sql = "SELECT * FROM Guns";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- List of Guns ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String caliber = rs.getString("caliber");
                System.out.printf("ID: %d, Name: %s, Type: %s, Caliber: %s%n", id, name, type, caliber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving Guns. Please try again.");
        }
    }
    private void updateGun() {
        System.out.print("Enter Gun ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Gun Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Gun Type: ");
        String type = scanner.nextLine();
        System.out.print("Enter new Gun Caliber: ");
        String caliber = scanner.nextLine();

        String sql = "UPDATE Guns SET name = ?, type = ?, caliber = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, caliber);
            pstmt.setInt(4, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Gun updated successfully!");
            } else {
                System.out.println("No Gun found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating Gun. Please check your input.");
        }
    }
    private void deleteGun() {
        System.out.print("Enter Gun ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        String sql = "DELETE FROM Guns WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Gun deleted successfully!");
            } else {
                System.out.println("No Gun found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting Gun. Please check your input.");
        }
    }
     private void closeResources() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error closing resources.");
        }
    }


    public static void main(String[] args) {
        Gun app = new Gun();
        app.start();
    }
}