package mapper;

import dto.response.TrainingTypeResponse;
import entity.TrainingType;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {
    public TrainingType toEntity(String spec) {
        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization(spec);
        return trainingType;
    }

    public TrainingTypeResponse toResponse(TrainingType trainingType) {
        TrainingTypeResponse response = new TrainingTypeResponse();
        response.setTrainingType(trainingType.getSpecialization());
        response.setTrainingId(trainingType.getId());
        return response;
    }
}
