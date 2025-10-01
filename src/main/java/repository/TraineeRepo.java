package repository;

import entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeRepo {
    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    Optional<Trainee> selectTrainee(String username);
    List<Trainee> selectByUsernameContaining(String username);
    void deleteTrainee(String username);
}
