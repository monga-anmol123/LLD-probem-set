package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import enums.SeatStatus;

/**
 * Represents a screen in a theater
 */
public class Screen {
    private String screenId;
    private String name;
    private Theater theater;
    private Map<String, Seat> seats;
    private List<Show> shows;

    public Screen(String screenId, String name, Theater theater) {
        this.screenId = screenId;
        this.name = name;
        this.theater = theater;
        this.seats = new HashMap<>();
        this.shows = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        seats.put(seat.getSeatId(), seat);
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    public List<Seat> getAllSeats() {
        return new ArrayList<>(seats.values());
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats.values()) {
            seat.checkAndReleaseLock();
            if (seat.getStatus() == SeatStatus.AVAILABLE) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    public int getTotalSeats() {
        return seats.size();
    }

    public String getScreenId() {
        return screenId;
    }

    public String getName() {
        return name;
    }

    public Theater getTheater() {
        return theater;
    }

    public List<Show> getShows() {
        return new ArrayList<>(shows);
    }

    @Override
    public String toString() {
        return "Screen{" +
                "screenId='" + screenId + '\'' +
                ", name='" + name + '\'' +
                ", totalSeats=" + seats.size() +
                '}';
    }
}


