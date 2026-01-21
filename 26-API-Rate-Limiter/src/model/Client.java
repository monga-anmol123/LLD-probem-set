package model;

import enums.ClientTier;

/**
 * Represents a client/user making API requests.
 */
public class Client {
    private final String clientId;
    private final String name;
    private final ClientTier tier;
    private final String apiKey;
    
    public Client(String clientId, String name, ClientTier tier) {
        this.clientId = clientId;
        this.name = name;
        this.tier = tier;
        this.apiKey = generateApiKey(clientId);
    }
    
    private String generateApiKey(String clientId) {
        return "sk_" + clientId + "_" + System.currentTimeMillis();
    }
    
    // Getters
    public String getClientId() {
        return clientId;
    }
    
    public String getName() {
        return name;
    }
    
    public ClientTier getTier() {
        return tier;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    @Override
    public String toString() {
        return name + " (" + tier + ", " + tier.getMaxRequests() + " req/" + tier.getWindowSeconds() + "s)";
    }
}

