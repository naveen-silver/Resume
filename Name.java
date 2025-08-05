
import java.sql.*;
import java.util.Scanner;

public class Name{

   String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
 String DB_URL = "jdbc:mysql://localhost:3306/Employee_details";
   String USER = "root";
   String PASS = "root";

    private Connection connection;
    private Scanner scanner;

    public Name() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully!");
            scanner = new Scanner(System.in);
        } catch (SQLException se) {
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
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployee();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }while(choice != 5);
         closeResources();
    }
    private void displayMenu() {
        System.out.println("\nEmployee Management System");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Exit");
    }
    public void addEmployee(){
        System.out.print("Enter reg_no: ");
        int reg_no = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Salary: ");
        double Salary = scanner.nextDouble();

        String sql = "INSERT INTO Employee (reg_no, name, Salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reg_no);
            pstmt.setString(2, name);
            pstmt.setDouble(3, Salary);
            pstmt.executeUpdate();
            System.out.println("Employee store successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding Employee Please check your input.");
        }
    }
    public void viewAllEmployee() {
        String sql = "SELECT * FROM Employee";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Employee List:");
            while (rs.next()) {
                int reg_no = rs.getInt("reg_no");
                String name = rs.getString("name");
                double Salary = rs.getDouble("Salary");
                System.out.printf("Reg No: %d, Name: %s, Salary: %.2f%n", reg_no, name, Salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving Employee list.");
        }
    }
    public void updateEmployee(){
        System.out.print("Enter reg_no of the Employee to update: ");
        int reg_no = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Salary: ");
        double Salary = scanner.nextDouble();

        String sql = "UPDATE Employee SET name = ?, Salary = ? WHERE reg_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, Salary);
            pstmt.setInt(3, reg_no);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No Employee found with reg_no: " + reg_no);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating Employee. Please check your input.");
        }
    }
    public void deleteEmployee() {
        System.out.print("Enter reg_no of the Employee to delete: ");
        int reg_no = scanner.nextInt();

        String sql = "DELETE FROM Employee WHERE reg_no = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reg_no);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No Employee found with reg_no: " + reg_no);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting Employee. Please check your input.");
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
            System.err.println("Error closing database connection.");
        }
    }
    public static void main(String[] args) {
        Name app = new Name();
        app.start();
        
    }
}