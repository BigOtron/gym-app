package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;

    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings = new ArrayList<>();

    public Trainee(String firstName, String lastName, String username, String password,
                   Date dateOfBirth, String address) {
        super(firstName, lastName, username, password);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
