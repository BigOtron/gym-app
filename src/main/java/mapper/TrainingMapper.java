package mapper;

import dto.request.CreateTrainingRequest;
import entity.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public Training toTraining(CreateTrainingRequest request) {
        Training training = new Training();
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());
        return training;
    }
}
