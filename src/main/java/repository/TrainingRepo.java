package repository;

import entity.Training;

import java.util.Optional;
import java.util.UUID;

public interface TrainingRepo {
    void createTraining(Training training);
    Optional<Training> selectTraining(UUID id);
}
