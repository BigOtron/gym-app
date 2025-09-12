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

    public long getUserId() {
        return userId;
    }

    public Trainee setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Trainee setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Trainee setAddress(Address address) {
        this.address = address;
        return this;
    }
}
