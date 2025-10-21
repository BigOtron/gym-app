package mapper;

import dto.request.TraineeRegRequest;
import dto.response.RegResponse;
import entity.Trainee;
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
}
