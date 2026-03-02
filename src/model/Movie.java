package model;
   
public class Movie {
    private String title;
    private double price;
    private String releaseDate;

    public Movie(String title, double price, String releaseDate) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    public Movie() {
        this.title = "";
        this.price = 0.0;
        this.releaseDate = "";
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    @Override
    public String toString() {
        return title + " ($" + price + ", Released: " + releaseDate + ")";
    }
}
