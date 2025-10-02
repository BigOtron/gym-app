package repository;

import entity.Trainer;
import entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainerRepo {
    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> selectTrainer(String username);
    List<Trainer> selectByUsernameContaining(String username);
    List<Training> selectTrainingsByUsername(String username);
    List<Training> selectTrainingsByTraineeFirstName(String firstName);
}
