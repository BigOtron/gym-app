package dao;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Repository
@Scope(SCOPE_SINGLETON)
public class TraineeDAO {
    private final Map<Long, Trainee> traineeMap;

    public TraineeDAO(Map<Long, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }

    public void createTrainee(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
    }

    public void updateTrainee(Trainee trainee) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(trainee.getUserId())) {
            throw new NoSuchTraineeException();
        }
        traineeMap.put(trainee.getUserId(), trainee);
    }

    public Trainee selectTrainee(long id) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(id)) {
            throw new NoSuchTraineeException();
        }
        return traineeMap.get(id);
    }

    public Trainee deleteTrainee(long id) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(id)) {
            throw new NoSuchTraineeException();
        }
        return traineeMap.remove(id);
    }
}
