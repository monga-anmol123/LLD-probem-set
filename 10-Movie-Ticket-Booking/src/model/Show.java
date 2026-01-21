package model;

import enums.ShowStatus;
import enums.SeatStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a movie show in a screen
 */
public class Show {
    private String showId;
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ShowStatus status;
    private Map<String, Seat> showSeats; // Copy of screen seats for this show

    public Show(String showId, Movie movie, Screen screen, LocalDateTime startTime) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.getDurationMinutes());
        this.status = ShowStatus.SCHEDULED;
        this.showSeats = new HashMap<>();
        
        // Create a copy of screen seats for this show
        for (Seat screenSeat : screen.getAllSeats()) {
            Seat showSeat = new Seat(
                screenSeat.getSeatId(),
                screenSeat.getRow(),
                screenSeat.getColumn(),
                screenSeat.getType(),
                screenSeat.getBasePrice()
            );
            showSeats.put(showSeat.getSeatId(), showSeat);
        }
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : showSeats.values()) {
            seat.checkAndReleaseLock();
            if (seat.getStatus() == SeatStatus.AVAILABLE) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    public Seat getSeat(String seatId) {
        return showSeats.get(seatId);
    }

    public List<Seat> getAllSeats() {
        return new ArrayList<>(showSeats.values());
    }

    public boolean lockSeats(List<String> seatIds, String userId, int lockDurationMinutes) {
        // First check if all seats can be locked
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            if (seat == null) {
                return false;
            }
            seat.checkAndReleaseLock();
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                return false;
            }
        }

        // Lock all seats
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            if (!seat.lock(userId, lockDurationMinutes)) {
                // Rollback if any seat fails to lock
                rollbackLocks(seatIds, userId);
                return false;
            }
        }

        return true;
    }

    private void rollbackLocks(List<String> seatIds, String userId) {
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            if (seat != null && seat.getLockedByUserId() != null && 
                seat.getLockedByUserId().equals(userId)) {
                seat.release();
            }
        }
    }

    public boolean bookSeats(List<String> seatIds, String userId) {
        // Check all seats are locked by this user
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            if (seat == null || seat.getStatus() != SeatStatus.LOCKED || 
                !userId.equals(seat.getLockedByUserId())) {
                return false;
            }
        }

        // Book all seats
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            seat.book(userId);
        }

        return true;
    }

    public void releaseSeats(List<String> seatIds) {
        for (String seatId : seatIds) {
            Seat seat = showSeats.get(seatId);
            if (seat != null) {
                seat.release();
            }
        }
    }

    public int getAvailableSeatsCount() {
        return getAvailableSeats().size();
    }

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showId='" + showId + '\'' +
                ", movie=" + movie.getTitle() +
                ", screen=" + screen.getName() +
                ", startTime=" + startTime +
                ", status=" + status +
                ", availableSeats=" + getAvailableSeatsCount() + "/" + showSeats.size() +
                '}';
    }
}


