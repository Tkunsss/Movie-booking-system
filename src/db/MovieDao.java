package db;

import model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// OOP: DAO pattern (separates DB access from UI/logic).
public class MovieDao {

    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title, price, release_date FROM movies";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("release_date")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load movies failed", e);
        }
        return movies;
    }

    public int insert(Movie movie) {
        String sql = "INSERT INTO movies(title, price, release_date) VALUES(?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getTitle());
            ps.setDouble(2, movie.getPrice());
            ps.setString(3, movie.getReleaseDate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    movie.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Insert movie failed", e);
        }
        return 0;
    }

    public void update(Movie movie) {
        String sql = "UPDATE movies SET title=?, price=?, release_date=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setDouble(2, movie.getPrice());
            ps.setString(3, movie.getReleaseDate());
            ps.setInt(4, movie.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update movie failed", e);
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM movies WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete movie failed", e);
        }
    }
}
