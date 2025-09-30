package service;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.TraineeRepo;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepo traineeRepository;

    public void createTrainee(Trainee trainee) {
        traineeRepository.createTrainee(trainee);
    }

    public void updateTrainee(Trainee trainee) {
        traineeRepository.updateTrainee(trainee);
    }

    public Trainee selectTrainee(String username) throws NoSuchTraineeException {
        return traineeRepository
                .selectTrainee(username)
                .orElseThrow(NoSuchTraineeException::new);
    }

    public void deleteTrainee(String username) {
        traineeRepository.deleteTrainee(username);
    }
}
