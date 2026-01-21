package strategy;

public class EditorPermission implements PermissionStrategy {
    
    @Override
    public boolean canRead() {
        return true;
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    @Override
    public boolean canDelete() {
        return false;
    }
    
    @Override
    public boolean canShare() {
        return false;
    }
    
    @Override
    public String getPermissionLevel() {
        return "EDITOR";
    }
}
