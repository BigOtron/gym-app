package repository;

import entity.Trainee;
import entity.Trainer;
import entity.Training;

import java.util.List;
import java.util.Optional;

public interface TraineeRepo {
    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    Optional<Trainee> selectTrainee(String username);
    List<Trainee> selectByUsernameContaining(String username);
    List<Training> selectTrainingsByUsername(String username);
    List<Training> selectTrainingsByTrainerFirstName(String firstName);
    List<Trainer> selectTrainersNotAssignedByUsername(String username);
    void deleteTrainee(String username);
}
