package service;

import dto.request.TraineeRegRequest;
import dto.response.RegResponse;
import entity.Trainee;
import exceptions.NoSuchTraineeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.TraineeRepo;
import static utility.PasswordGenerator.generatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepo traineeRepository;

    public RegResponse createTrainee(TraineeRegRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress()); // TODO: please, please, create a mapper later
        trainee.setPasswordHash(generatePassword(10));
        trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName());
        log.info("Creating trainee with tentative username={}", trainee.getUsername());

        if (traineeRepository.selectTrainee(trainee.getUsername()).isPresent()) {
            int size = traineeRepository.selectByUsernameContaining(trainee.getUsername()).size();
            trainee.setUsername(size + trainee.getFirstName() + "." + trainee.getLastName());
            log.debug("Username exists, generated new username={}", trainee.getUsername());
        }

        traineeRepository.createTrainee(trainee);
        log.info("Trainee created successfully with username={}", trainee.getUsername());
        return new RegResponse(trainee.getUsername(), trainee.getPasswordHash());
    }

    public void updateTrainee(Trainee trainee) {
        log.info("Updating trainee username={}", trainee.getUsername());
        traineeRepository.updateTrainee(trainee);
    }

    public Trainee selectTrainee(String username) throws NoSuchTraineeException {
        log.debug("Selecting trainee by username={}", username);
        return traineeRepository
                .selectTrainee(username)
                .orElseThrow(() -> {
                    log.warn("No trainee found with username={}", username);
                    return new NoSuchTraineeException();
                });
    }

    public void activateTrainee(String username) throws NoSuchTraineeException {
        log.info("Activating trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.TRUE);
        updateTrainee(trainee);
        log.info("Trainee username={} activated", username);
    }

    public void deactivateTrainee(String username) throws NoSuchTraineeException {
        log.info("Deactivating trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.FALSE);
        updateTrainee(trainee);
        log.info("Trainee username={}", username);
    }

    public void deleteTrainee(String username) {
        log.info("Deleting trainee={}", username);
        traineeRepository.deleteTrainee(username);
        log.info("Trainee username={} deleted", username);
    }

    public boolean validateTraineeCredentials(String username, String password) throws NoSuchTraineeException {
        log.debug("Validating credentials for trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        boolean isValid = trainee.getPasswordHash().equals(password);
        log.info("Credentials validation for username={} result={}", username, isValid);
        return isValid;
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws NoSuchTraineeException {
        log.info("Changing password for trainee username={}", username);
        Trainee trainee = selectTrainee(username);

        if (!trainee.getPasswordHash().equals(oldPassword)) {
            log.warn("Password change failed: incorrect old password for username={}", username);
            throw new IllegalArgumentException("Old password is incorrect");
        }

        trainee.setPasswordHash(newPassword);
        traineeRepository.updateTrainee(trainee);
        log.info("Password changed successfully for trainee username={}", username);
    }
}
