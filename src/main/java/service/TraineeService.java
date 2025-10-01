package service;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.TraineeRepo;
import static utility.PasswordGenerator.generatePassword;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepo traineeRepository;

    public void createTrainee(Trainee trainee) {
        trainee.setPasswordHash(generatePassword(10));
        if (traineeRepository.selectTrainee(trainee.getUsername()).isEmpty()) {
            trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName());
        } else {
            int size = traineeRepository.selectByUsernameContaining(trainee.getUsername()).size();
            trainee.setUsername(size + trainee.getFirstName() + "." + trainee.getLastName());
        }
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

    public void activateTrainee(String username) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.TRUE);
        updateTrainee(trainee);
    }

    public void deactivateTrainee(String username) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.FALSE);
        updateTrainee(trainee);
    }

    public void deleteTrainee(String username) {
        traineeRepository.deleteTrainee(username);
    }

    public boolean validateTraineeCredentials(String username, String password) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(username);
        return trainee.getPasswordHash().equals(password);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(username);
        if (!trainee.getPasswordHash().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        trainee.setPasswordHash(newPassword);
        traineeRepository.updateTrainee(trainee);
    }
}
