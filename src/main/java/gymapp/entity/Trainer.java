package gymapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Trainer extends User{
    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    @NotNull(message = "Specialization is required")
    private TrainingType specialization;


    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings = new ArrayList<>();

    public Trainer(String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }
}
