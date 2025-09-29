package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainee trainee)) return false;
        return Objects.equals(getFirstName(), trainee.getFirstName()) &&
                Objects.equals(getLastName(), trainee.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
