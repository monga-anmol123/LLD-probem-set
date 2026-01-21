package observer;

import model.Ride;

public interface Observer {
    void update(Ride ride, String message);
}

