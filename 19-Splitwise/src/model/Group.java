package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String name;
    private List<User> members;
    private List<Expense> expenses;
    
    public Group(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }
    
    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }
    
    public void removeMember(User user) {
        members.remove(user);
    }
    
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
    
    public boolean isMember(User user) {
        return members.contains(user);
    }
    
    // Getters
    public String getGroupId() { return groupId; }
    public String getName() { return name; }
    public List<User> getMembers() { return new ArrayList<>(members); }
    public List<Expense> getExpenses() { return new ArrayList<>(expenses); }
    
    @Override
    public String toString() {
        return String.format("Group[%s]: %s (%d members, %d expenses)", 
            groupId, name, members.size(), expenses.size());
    }
}
