package dao;


import entity.Training;
import exceptions.NoSuchTrainingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import storage.TrainingStorage;

import java.util.Map;

/**
 * DAO for managing {@link Training} entities.
 * <p>
 * Provides methods to create and retrieve trainings stored in the underlying
 * {@link TrainingStorage}. Maintains an internal map of training IDs to Training objects.
 */
@Repository
public class TrainingDAO {
    private final Map<Long, Training> trainingMap;
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);

    public TrainingDAO(@Qualifier("trainingStorage")TrainingStorage storage) {
        this.trainingMap = storage.getStorage();
        logger.info("TrainingDAO initialized with {} trainings", trainingMap.size());
    }

    public void createTraining(Training training) {
        trainingMap.put(training.getTrainingId(), training);
        logger.info("Created training with ID {} and name '{}'", training.getTrainingId(), training.getTrainingName());
    }

    public Training selectTraining(long id) throws NoSuchTrainingException {
        if (!trainingMap.containsKey(id)) {
            logger.warn("Training with ID {} not found", id);
            throw new NoSuchTrainingException();
        }
        Training training = trainingMap.get(id);
        logger.debug("Retrieved training with ID {}: {}", id, training.getTrainingName());
        return training;
    }
}
