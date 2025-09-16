package service;

import dao.TrainingDAO;
import entity.Training;
import exceptions.NoSuchTrainingException;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private final TrainingDAO trainingDAO;

    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public void createTraining(Training training) {
        trainingDAO.createTraining(training);
    }

    public Training selectTraining(long id) {
        try {
            return trainingDAO.selectTraining(id);
        } catch (NoSuchTrainingException e) {
            // we will notify the caller
            throw new RuntimeException(e);
        }
    }
}
