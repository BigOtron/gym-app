package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Trainer extends User{
    private long userId;
    private List<TrainingType> specialization;

    public Trainer(String firstName, String lastName, String username,
                   String password, List<TrainingType> specialization) {
        super(firstName, lastName, username, password);
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        return Objects.equals(getFirstName(), trainer.getFirstName()) &&
                Objects.equals(getLastName(), trainer.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
