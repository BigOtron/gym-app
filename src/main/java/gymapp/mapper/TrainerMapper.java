package gymapp.mapper;

import gymapp.dto.request.TrainerRegRequest;
import gymapp.dto.request.UpdateTrainerProfileRequest;
import gymapp.dto.response.RegResponse;
import gymapp.dto.response.TrainerProfileResponse;
import gymapp.dto.response.TrainerTrainingsResponse;
import gymapp.entity.Trainee;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
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

    public Trainer toUpdatedEntity(UpdateTrainerProfileRequest request, Trainer trainer) {
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setIsActive(request.isActive());
        return trainer;
    }

    public TrainerTrainingsResponse toTrainings(Training t) {
        TrainerTrainingsResponse response = new TrainerTrainingsResponse();
        response.setTrainingName(t.getTrainingName());
        response.setTrainingDate(t.getTrainingDate());
        response.setTrainingType(t.getTrainingType().getSpecialization());
        response.setDuration(t.getDuration());
        response.setTraineeName(t.getTrainee().getFirstName());

        return response;
    }
}
