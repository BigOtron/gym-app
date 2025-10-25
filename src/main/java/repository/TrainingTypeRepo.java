package repository;

import entity.TrainingType;

import java.util.List;

public interface TrainingTypeRepo {
    void createTrainingType(TrainingType trainingType);
    List<TrainingType> selectAll();
}
