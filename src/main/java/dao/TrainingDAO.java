package dao;


import entity.Training;
import exceptions.NoSuchTrainingException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import java.util.Map;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Repository
@Scope(SCOPE_SINGLETON)
public class TrainingDAO {
    private final Map<Long, Training> trainingMap;

    public TrainingDAO(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    public void createTraining(Training training) {
        trainingMap.put(training.getTrainingId(), training);
    }

    public Training selectTraining(long id) throws NoSuchTrainingException {
        if (!trainingMap.containsKey(id)) {
            throw new NoSuchTrainingException();
        }
        return trainingMap.get(id);
    }
}
