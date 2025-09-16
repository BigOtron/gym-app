package dao;

import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import storage.TrainerStorage;

import java.util.Map;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Repository
@Scope(SCOPE_SINGLETON)
public class TrainerDAO {
    private final Map<Long, Trainer> trainerMap;
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);

    @Autowired
    public TrainerDAO(@Qualifier("trainerStorage")  TrainerStorage storage) {
        trainerMap = storage.getStorage();
        logger.info("TrainerDAO initialized with {} trainers", trainerMap.size());
    }

    public void createTrainer(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
        logger.info("Created trainer with ID {} and username '{}'",
                trainer.getUserId(), trainer.getUsername());
    }

    public void updateTrainer(Trainer trainer) throws NoSuchTrainerException {
        if (!trainerMap.containsKey(trainer.getUserId())) {
            logger.warn("Cannot update: Trainer with ID {} does not exist", trainer.getUserId());
            throw new NoSuchTrainerException();
        }
        trainerMap.put(trainer.getUserId(), trainer);
        logger.info("Updated trainer with ID {} and username '{}'",
                trainer.getUserId(), trainer.getUsername());
    }

    public Trainer selectTrainer(long id) throws NoSuchTrainerException {
        if (!trainerMap.containsKey(id)) {
            logger.warn("Trainer with ID {} not found", id);
            throw new NoSuchTrainerException();
        }
        Trainer trainer = trainerMap.get(id);
        logger.debug("Retrieved trainer with ID {} and username '{}'",
                id, trainer.getUsername());
        return trainer;


    }
}
