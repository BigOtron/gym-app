package dao;

import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import java.util.Map;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Repository
@Scope(SCOPE_SINGLETON)
public class TrainerDAO {
    private final Map<Long, Trainer> trainerMap;

    public TrainerDAO(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    public void createTrainer(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
    }

    public void updateTrainer(Trainer trainer) throws NoSuchTrainerException {
        if (!trainerMap.containsKey(trainer.getUserId())) {
            throw new NoSuchTrainerException();
        }
        trainerMap.put(trainer.getUserId(), trainer);
    }

    public Trainer selectTrainer(long id) throws NoSuchTrainerException {
        if (trainerMap.containsKey(id)) {
            return trainerMap.get(id); // TODO - is it necessary to do defensive copying here?
        }
        throw new NoSuchTrainerException();

    }
}
