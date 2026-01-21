package observer;

import model.TrafficSignal;
import enums.SignalLight;

/**
 * Concrete Observer: Digital display board at intersection
 * Shows current signal status to drivers
 */
public class DisplayBoard implements TrafficObserver {
    private String boardId;
    
    public DisplayBoard(String boardId) {
        this.boardId = boardId;
    }
    
    @Override
    public void update(TrafficSignal signal, SignalLight newLight) {
        System.out.println("  [Display Board " + boardId + "] " + 
                         signal.getDirection() + " signal changed to " + newLight);
        displayLight(newLight);
    }
    
    private void displayLight(SignalLight light) {
        switch (light) {
            case RED:
                System.out.println("  [Display Board " + boardId + "] 🔴 STOP");
                break;
            case YELLOW:
                System.out.println("  [Display Board " + boardId + "] 🟡 CAUTION");
                break;
            case GREEN:
                System.out.println("  [Display Board " + boardId + "] 🟢 GO");
                break;
        }
    }
    
    public String getBoardId() {
        return boardId;
    }
}


