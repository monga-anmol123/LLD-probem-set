package model;

public class StandardMember extends Member {
    public StandardMember(String memberId, String name, String email) {
        super(memberId, name, email);
        this.maxBooksAllowed = 5;
    }
}


