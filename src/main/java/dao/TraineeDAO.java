package dao;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import storage.TraineeStorage;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Repository
@Scope(SCOPE_SINGLETON)
public class TraineeDAO {
    private final Map<Long, Trainee> traineeMap;
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    public TraineeDAO(@Qualifier("traineeStorage")TraineeStorage storage) {
        this.traineeMap = storage.getStorage();
        logger.info("TrainingDAO initialized with {} trainees", traineeMap.size());
    }

    public void createTrainee(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
        logger.info("Created training with ID {} and username '{}'",
                trainee.getUserId(), trainee.getUsername());
    }

    public void updateTrainee(Trainee trainee) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(trainee.getUserId())) {
            logger.warn("Cannot update: Trainee with ID {} does not exist", trainee.getUserId());
            throw new NoSuchTraineeException();
        }
        traineeMap.put(trainee.getUserId(), trainee);
        logger.info("Updated trainee with ID {} and username '{}'",
                trainee.getUserId(), trainee.getUsername());
    }

    public Trainee selectTrainee(long id) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(id)) {
            logger.warn("Trainee with ID {} not found", id);
            throw new NoSuchTraineeException();
        }
        Trainee trainee = traineeMap.get(id);
        logger.debug("Retrieved trainee with ID {} and username '{}'",
                id, trainee.getUsername());
        return trainee;
    }

    public Trainee deleteTrainee(long id) throws NoSuchTraineeException {
        if (!traineeMap.containsKey(id)) {
            logger.warn("Trainee with ID {} could not be found", id);
            throw new NoSuchTraineeException();
        }
        logger.info("Trainee with ID {} has been deleted", id);
        return traineeMap.remove(id);
    }
}
