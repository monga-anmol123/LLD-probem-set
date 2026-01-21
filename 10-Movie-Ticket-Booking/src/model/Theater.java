package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a theater with multiple screens
 */
public class Theater {
    private String theaterId;
    private String name;
    private String city;
    private String address;
    private Map<String, Screen> screens;

    public Theater(String theaterId, String name, String city, String address) {
        this.theaterId = theaterId;
        this.name = name;
        this.city = city;
        this.address = address;
        this.screens = new HashMap<>();
    }

    public void addScreen(Screen screen) {
        screens.put(screen.getScreenId(), screen);
    }

    public Screen getScreen(String screenId) {
        return screens.get(screenId);
    }

    public List<Screen> getAllScreens() {
        return new ArrayList<>(screens.values());
    }

    public String getTheaterId() {
        return theaterId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "theaterId='" + theaterId + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", screens=" + screens.size() +
                '}';
    }
}


