package model;

public class PremiumMember extends Member {
    public PremiumMember(String memberId, String name, String email) {
        super(memberId, name, email);
        this.maxBooksAllowed = 10;
    }
}


