package com.example.flattie.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String joinCode; // Randomly generated code for joining the flat
    private String flatName; // Name of the flat

    @Column(unique = true) // Ensure the address is unique in the database
    private String address; // Address of the flat

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    private List<FlatMembership> members; // List of admin members

    private String city; // City where the flat is located
    private String postcode; // Postcode of the flat
    private String flatDescription; // Description of the flat
    private double weeklyRent; // Weekly rent for the flat
    private int rooms; // Number of rooms in the flat

    @ManyToOne
    @JoinColumn(name = "property_manager_id")
    private PropertyManager propertyManager; // Property manager associated with the flat

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceTicket> maintenanceTickets = new ArrayList<>(); // List of maintenance tickets associated
                                                                            // with the flat

    @OneToOne(mappedBy = "flat", cascade = CascadeType.ALL)
    private ChoreList choreList; // Chore list associated with the flat

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rent_expense_id")
    private FlatExpense rentExpense; // Rent expense associated with the flat

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlatExpense> expenses = new ArrayList<>(); // List of expenses associated with the flat

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppUser> users = new ArrayList<>(); // List of users associated with the flat

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Notice> noticeBoard = new ArrayList<>(); // List of notices associated with the flat

    @OneToMany(mappedBy = "flat")
    private List<Event> events; // List of events on the flat calender

    // Default constructor required by JPA
    public Flat() {
    }

    // Constructor with all fields
    public Flat(String joinCode, String flatName, String address, String city, String postcode, String flatDescription,
            double weeklyRent, int rooms) {
        this.joinCode = joinCode;
        this.flatName = flatName;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.flatDescription = flatDescription;
        this.weeklyRent = weeklyRent;
        this.rooms = rooms;
        this.choreList = new ChoreList(this, new ArrayList<>()); // Initialize the chore list with an empty list
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public String getFlatName() {
        return flatName;
    }

    public void setFlatName(String flatName) {
        this.flatName = flatName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getFlatDescription() {
        return flatDescription;
    }

    public void setFlatDescription(String flatDescription) {
        this.flatDescription = flatDescription;
    }

    // TODO: refactor to phase out the weeklyRent field to use the rentExpense field
    // This is a temporary solution to allow for the rent expense to be set
    public double getWeeklyRent() {
        // Try to get a rent expense from the list
        if (rentExpense != null && rentExpense.getTotalAmount() != null) {
            double expenseAmount = rentExpense.getTotalAmount().doubleValue();
            return (expenseAmount > weeklyRent) ? expenseAmount : weeklyRent;
        }
        return weeklyRent;
    }

    public void setWeeklyRent(double weeklyRent) {
        this.weeklyRent = weeklyRent; // Set the weekly rent for the flat
        // Update the rent expense if it exists
        if (this.rentExpense != null) {
            this.rentExpense.setTotalAmount(BigDecimal.valueOf(weeklyRent));
        } else {
            // Create a new rent expense if it doesn't exist
            this.rentExpense = new FlatExpense(this, "Rent", BigDecimal.valueOf(weeklyRent), null);
        }
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public ChoreList getChoreList() {
        return choreList;
    }

    public void setChoreList(ChoreList choreList) {
        this.choreList = choreList;
    }

    public FlatExpense getRentExpense() {
        return rentExpense;
    }

    public void setRentExpense(FlatExpense rentExpense) {
        this.rentExpense = rentExpense;
    }

    public List<FlatExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<FlatExpense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(FlatExpense expense) {
        expenses.add(expense); // Add the expense to the list
        expense.setFlat(this); // Set the flat reference in the expense

        // If the expense is a rent expense, set it as the rent expense for the flat
        if ("rent".equalsIgnoreCase(expense.getName())) {
            this.rentExpense = expense;
        }
    }

    public void removeExpense(FlatExpense expense) {
        expenses.remove(expense);
        expense.setFlat(null); // Remove the flat reference from the expense
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public void addUser(AppUser user) {
        this.users.add(user);
        user.setFlat(this); // Ensure bidirectional consistency
    }

    public void removeUser(AppUser user) {
        this.users.remove(user);
        user.setFlat(null); // Ensure bidirectional consistency
    }

    public void addMaintenanceTicket(MaintenanceTicket ticket) {
        maintenanceTickets.add(ticket); // Add the ticket to the list
        ticket.setFlat(this); // Set the flat reference in the ticket
    }

    public void removeMaintenanceTicket(MaintenanceTicket ticket) {
        maintenanceTickets.remove(ticket); // Remove the ticket from the list
        ticket.setFlat(null); // Remove the flat reference from the ticket
    }

    public List<MaintenanceTicket> getMaintenanceTickets() {
        return maintenanceTickets; // Return the list of maintenance tickets
    }

    public void setMaintenanceTickets(List<MaintenanceTicket> maintenanceTickets) {
        this.maintenanceTickets = maintenanceTickets; // Set the list of maintenance tickets
    }

    public PropertyManager getPropertyManager() {
        return propertyManager; // Return the property manager for the flat
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager; // Set the property manager for the flat
    if (propertyManager != null && !propertyManager.getFlats().contains(this)) { // Check if the property manager already has this flat
        propertyManager.addFlat(this); // bi-directional relationship
    }
    }

    public List<Notice> getNoticeBoard() {
        return noticeBoard;
    }

    public void setNoticeBoard(List<Notice> noticeBoard) {
        this.noticeBoard = noticeBoard;
    }

    public void addNotice(Notice notice) {
        noticeBoard.add(notice);
    }

    public void removeNotice(Notice notice) {
        System.out.println(noticeBoard.get(0).toString());
        System.out.println(notice.toString());
        noticeBoard.remove(notice);
    }

    public List<FlatMembership> getMembers() {
        return members; // Return the list of flat members
    }

    public void setMembers(List<FlatMembership> members) {
        this.members = members; // Set the list of flat members
    }

    public void addMember(FlatMembership member) {
        this.members.add(member); // Add the member to the list

    }

    public void removeMember(FlatMembership member) {
        this.members.remove(member); // Remove the member from the list

    }

    public Optional<AppUser> getOwner() {
        return members.stream()
                .filter(m -> m.getRole().equals(Role.OWNER)) // Filter for the owner role
                .map(FlatMembership::getUser)
                .findFirst();
    }

    public List<AppUser> getAdmins() {
        return members.stream()
                .filter(m -> m.getRole() == Role.ADMIN)
                .map(FlatMembership::getUser)
                .collect(Collectors.toList());
    }

    public List<AppUser> getMembersByRole(Role role) {
        return members.stream()
                .filter(m -> m.getRole() == role)
                .map(FlatMembership::getUser)
                .collect(Collectors.toList());
    }

    public boolean isAdmin(AppUser user) {
        return members.stream()
                .anyMatch(m -> m.getUser().equals(user) && m.getRole() == Role.ADMIN);
    }

    public Optional<Role> getRoleOfUser(AppUser user) {
        return members.stream()
                .filter(m -> m.getUser().equals(user))
                .map(m -> (Role) m.getRole())
                .findFirst();
    }

}
