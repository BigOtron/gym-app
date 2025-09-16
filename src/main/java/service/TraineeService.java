package service;

import dao.TraineeDAO;
import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {
    private final TraineeDAO traineeDAO;

    public TraineeService(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public void createTrainee(Trainee trainee) {
        traineeDAO.createTrainee(trainee);
    }

    public void updateTrainee(Trainee trainee) {
        try {
            traineeDAO.updateTrainee(trainee);
        } catch (NoSuchTraineeException e) {
            // we will inform the call about this
            throw new RuntimeException(e);
        }
    }

    public Trainee selectTrainee(long id) {
        try {
            return traineeDAO.selectTrainee(id);
        } catch (NoSuchTraineeException e) {
            // we will inform the call about this
            throw new RuntimeException(e);
        }
    }

    public Trainee deleteTrainee(long id) {
        try {
            return traineeDAO.deleteTrainee(id);
        } catch (NoSuchTraineeException e) {
            // we will inform the call about this
            throw new RuntimeException(e);
        }
    }
}
