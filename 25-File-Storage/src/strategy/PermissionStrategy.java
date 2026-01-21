package strategy;

public interface PermissionStrategy {
    boolean canRead();
    boolean canWrite();
    boolean canDelete();
    boolean canShare();
    String getPermissionLevel();
}
