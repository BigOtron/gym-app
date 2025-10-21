package mapper;

import dto.request.TrainerRegRequest;
import dto.response.RegResponse;
import entity.Trainer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainerMapper {
    private final TrainingTypeMapper trainingTypeMapper;

    public Trainer toEntity(TrainerRegRequest request) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setSpecialization(trainingTypeMapper.toEntity(request.getSpecialization()));
        return trainer;
    }

    public RegResponse toRegResponse(String username, String password) {
        return new RegResponse(username, password);
    }
}
