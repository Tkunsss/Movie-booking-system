package service;

import model.Movie;

// OOP: Base class for tickets (inheritance).
// OOP: Polymorphism via overridden calculatePrice() in subclasses.
// Base ticket class.
public class Ticket {

    // Movie and seat for this ticket.
    // Composition: Ticket has a Movie reference.
    protected Movie movie;
    protected int seatNumber;

    // Make a ticket with movie and seat.
    public Ticket(Movie movie, int seatNumber) {
        this.movie = movie;
        this.seatNumber = seatNumber;
    }

    // Price for this ticket.
    public double calculatePrice() {
        return movie.getPrice();
    }

    // Get movie.
    public Movie getMovie() { return movie; }

    // Get seat number.
    public int getSeatNumber() { return seatNumber; }

    // Ticket type name.
    public String getType() {
        return "Ticket";
    }

    // Show ticket as text.
    @Override
    public String toString() {
        return getType() + " | Movie: " + movie.getTitle() +
               " | Seat: " + seatNumber +
               " | Price: $" + calculatePrice();
    }
}
