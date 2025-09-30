package entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    private String specialization;

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers = new ArrayList<>();


    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings = new ArrayList<>();
}
