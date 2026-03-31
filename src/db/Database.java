package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// OOP: Static utility (shared DB connection helpers).
public class Database {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String DB_NAME = "movie_booking_system_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/?serverTimezone=UTC&useSSL=false";
    private static final String DB_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?serverTimezone=UTC&useSSL=false";

    public static Connection getConnection() throws SQLException {
        loadDriver();
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void init() {
        loadDriver();
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            throw new RuntimeException("DB create failed", e);
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String moviesSql = """
                CREATE TABLE IF NOT EXISTS movies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    price DOUBLE NOT NULL,
                    release_date VARCHAR(20) NOT NULL
                );
                """;
            String staffSql = """
                CREATE TABLE IF NOT EXISTS staff (
                    staff_id VARCHAR(20) PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    username VARCHAR(50) NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    position VARCHAR(50) NOT NULL,
                    active TINYINT NOT NULL
                );
                """;
            String customersSql = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id VARCHAR(20) PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    balance DOUBLE NOT NULL,
                    active TINYINT NOT NULL,
                    created_by_staff_id VARCHAR(20)
                );
                """;
            String ordersSql = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    customer_id VARCHAR(20),
                    description VARCHAR(255),
                    status VARCHAR(30),
                    subtotal DOUBLE,
                    discount DOUBLE,
                    tax DOUBLE,
                    total DOUBLE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
            String orderItemsSql = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    order_id INT NOT NULL,
                    movie_id INT NOT NULL,
                    seat_number INT NOT NULL,
                    ticket_type VARCHAR(30) NOT NULL,
                    price DOUBLE NOT NULL
                );
                """;
            stmt.execute(moviesSql);
            stmt.execute(staffSql);
            stmt.execute(customersSql);
            stmt.execute(ordersSql);
            stmt.execute(orderItemsSql);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found. Check lib/mysql-connector-j-*.jar", e);
        }
    }
}
