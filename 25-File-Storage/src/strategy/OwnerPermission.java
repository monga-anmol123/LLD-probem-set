package strategy;

public class OwnerPermission implements PermissionStrategy {
    
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
        return true;
    }
    
    @Override
    public boolean canShare() {
        return true;
    }
    
    @Override
    public String getPermissionLevel() {
        return "OWNER";
    }
}
