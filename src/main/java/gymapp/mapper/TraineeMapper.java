package gymapp.mapper;

import gymapp.dto.request.TraineeRegRequest;
import gymapp.dto.request.UpdateTraineeProfileRequest;
import gymapp.dto.response.RegResponse;
import gymapp.dto.response.TraineeProfileResponse;
import gymapp.dto.response.TraineeTrainingsResponse;
import gymapp.entity.Trainee;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {
    public Trainee toEntity(TraineeRegRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());
        return trainee;
    }

    public RegResponse toRegResponse(String username, String password) {
        return new RegResponse(username, password);
    }

    public TraineeProfileResponse toProfile(Trainee trainee) {
        TraineeProfileResponse response = new TraineeProfileResponse();
        response.setFirstName(trainee.getFirstName());
        response.setLastName(trainee.getLastName());
        response.setDateOfBirth(trainee.getDateOfBirth().toString());
        response.setAddress(trainee.getAddress());
        response.setActive(trainee.getIsActive());
        return response;
    }

    public TraineeProfileResponse.TrainerProfile toTrainerProfile(Trainer trainer) {
        return new TraineeProfileResponse.TrainerProfile(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().getSpecialization()
        );
    }

    public Trainee toUpdatedEntity(UpdateTraineeProfileRequest request, Trainee trainee) {
        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());
        trainee.setIsActive(request.isActive());

        return trainee;
    }

    public TraineeTrainingsResponse toTraining(Training training) {
        TraineeTrainingsResponse response = new TraineeTrainingsResponse();
        response.setTrainingName(training.getTrainingName());
        response.setTrainingType(training.getTrainingType().getSpecialization());
        response.setTrainingDate(training.getTrainingDate());
        response.setTrainingDuration(training.getDuration());
        response.setTrainerName(training.getTrainer().getFirstName());
        return response;
    }
}
