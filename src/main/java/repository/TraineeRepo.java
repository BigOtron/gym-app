package repository;

import entity.Trainee;

import java.util.Optional;

public interface TraineeRepo {
    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    Optional<Trainee> selectTrainee(String username);
    void deleteTrainee(String username);
}
