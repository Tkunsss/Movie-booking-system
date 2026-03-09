package service;

import model.Movie;

public class Ticket {

    protected Movie movie;
    protected int seatNumber;

    public Ticket(Movie movie, int seatNumber) {
        this.movie = movie;
        this.seatNumber = seatNumber;
    }

    public double calculatePrice() {
        return movie.getPrice();
    }

    public String getType() {
        return "Ticket";
    }

    @Override
    public String toString() {
        return getType() + " | Movie: " + movie.getTitle() +
               " | Seat: " + seatNumber +
               " | Price: $" + calculatePrice();
    }
}
