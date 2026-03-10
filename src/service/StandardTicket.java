package service;

import model.Movie;

public class StandardTicket extends Ticket {

    public StandardTicket(Movie movie, int seatNumber) {
        super(movie, seatNumber);
    }

    @Override
    public double calculatePrice() {
        return movie.getPrice(); // normal price
    }

    @Override
    public String getType() {
        return "Standard Ticket";
    }
}
