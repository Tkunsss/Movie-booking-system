package model;

// OOP: Class = blueprint, object = Movie instance.

// OOP: Encapsulation via private fields + getters/setters.

// This class stores one movie.
public class Movie {
    // Encapsulation: fields are private; access through getters/setters.
    // DB id.
    private int id;
    // Movie name.
    private String title;
    // Movie price.
    private double price;
    // Movie release date as text.
    private String releaseDate;

    // Make a movie with data.
    public Movie(String title, double price, String releaseDate) {
        this(0, title, price, releaseDate);
    }

    // Make a movie with id.
    public Movie(int id, String title, double price, String releaseDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Make an empty movie.
    public Movie() {
        this(0, "", 0.0, "");
    }

    // Get movie id.
    public int getId() { return id; }
    // Set movie id.
    public void setId(int id) { this.id = id; }

    // Get movie title.
    public String getTitle() { return title; }
    // Set movie title.
    public void setTitle(String title) { this.title = title; }

    // Get movie price.
    public double getPrice() { return price; }
    // Set movie price.
    public void setPrice(double price) { this.price = price; }

    // Get movie release date.
    public String getReleaseDate() { return releaseDate; }
    // Set movie release date.
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    // Show movie as text.
    @Override
    public String toString() {
        return title + " ($" + price + ", Released: " + releaseDate + ")";
    }
}
