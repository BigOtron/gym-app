package dao;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import storage.TraineeStorage;

import java.util.Map;

/**
 * DAO for managing {@link Trainee} entities.
 * <p>
 * This class provides CRUD operations on trainees stored in the underlying
 * {@link TraineeStorage}. It maintains an internal map of trainee IDs to Trainee objects
 * <p>
 */
@Repository
public class TraineeDAO {
    private final Map<Long, Trainee> traineeMap;
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);

    public TraineeDAO(@Qualifier("traineeStorage")TraineeStorage storage) {
        this.traineeMap = storage.getStorage();
        logger.info("TrainingDAO initialized with {} trainees", traineeMap.size());
    }

    public void createTrainee(Trainee trainee) {
        if (traineeMap.containsValue(trainee)) {
            long count = traineeMap.values().stream()
                    .filter(s-> s.getFirstName().equals(trainee.getFirstName()))
                    .count();
            trainee.setUsername(count + trainee.getUsername());
        }
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
