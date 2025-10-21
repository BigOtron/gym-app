package mapper;

import entity.TrainingType;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeMapper {
    public TrainingType toEntity(String spec) {
        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization(spec);
        return trainingType;
    }
}
