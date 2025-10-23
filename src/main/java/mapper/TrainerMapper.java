package mapper;

import dto.request.TrainerRegRequest;
import dto.response.RegResponse;
import dto.response.TrainerProfileResponse;
import entity.Trainee;
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

    public TrainerProfileResponse toProfile(Trainer trainer) {
        TrainerProfileResponse response = new TrainerProfileResponse();
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setSpec(trainer.getSpecialization().getSpecialization());
        response.setActive(trainer.getIsActive());
        return response;
    }

    public TrainerProfileResponse.TraineeProfile toTraineeProfile(Trainee trainee) {
        return new TrainerProfileResponse.TraineeProfile(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName()
        );
    }
}
