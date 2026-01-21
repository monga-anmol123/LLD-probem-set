package observer;

import model.Elevator;
import enums.Direction;

/**
 * Concrete Observer: Display panel that shows elevator status.
 */
public class DisplayPanel implements ElevatorObserver {
    private String location;
    
    public DisplayPanel(String location) {
        this.location = location;
    }
    
    @Override
    public void update(Elevator elevator) {
        String directionSymbol = getDirectionSymbol(elevator.getDirection());
        String statusInfo = String.format(
            "[%s Display] Elevator %s: Floor %d %s | Status: %s | Load: %d/%d",
            location,
            elevator.getId(),
            elevator.getCurrentFloor(),
            directionSymbol,
            elevator.getStatus(),
            elevator.getCurrentLoad(),
            elevator.getCapacity()
        );
        System.out.println(statusInfo);
    }
    
    private String getDirectionSymbol(Direction direction) {
        switch (direction) {
            case UP: return "↑";
            case DOWN: return "↓";
            case IDLE: return "•";
            default: return "-";
        }
    }
}


