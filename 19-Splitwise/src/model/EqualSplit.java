package model;

import enums.SplitType;

public class EqualSplit extends Split {
    public EqualSplit(User user) {
        super(user, SplitType.EQUAL);
    }
}
