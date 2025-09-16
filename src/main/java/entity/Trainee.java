package entity;

import java.time.LocalDate;

public class Trainee extends User {
    private long userId;
    private LocalDate dateOfBirth;
    private Address address;

    public Trainee(String firstName, String lastName, String username, String password,
                   LocalDate dateOfBirth, Address address) {
        super(firstName, lastName, username, password);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee() {}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
