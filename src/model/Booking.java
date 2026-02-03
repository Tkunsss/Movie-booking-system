package model;

public class Booking {
    private Movie movie;
    private int quantity;

    public Booking(Movie movie, int quantity) {
        this.movie = movie;
        this.quantity = quantity;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return movie.getPrice() * quantity;
    }
}
