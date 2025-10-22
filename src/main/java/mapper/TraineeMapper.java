package mapper;

import dto.request.TraineeRegRequest;
import dto.request.UpdateTraineeProfileRequest;
import dto.response.RegResponse;
import dto.response.TraineeProfileResponse;
import entity.Trainee;
import entity.Trainer;
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
}
