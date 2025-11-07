package gymapp.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must be at most 100 characters")
    @Column(nullable = false, unique = true)
    private String specialization;

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers = new ArrayList<>();


    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings = new ArrayList<>();
}
