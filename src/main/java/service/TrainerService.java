package service;

import entity.Trainer;
import exceptions.NoSuchTrainerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.TrainerRepo;

import static utility.PasswordGenerator.generatePassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepo trainerRepository;

    public void createTrainer(Trainer trainer) {
        log.info("Creating trainer: {} {}", trainer.getFirstName(), trainer.getLastName());
        trainer.setPasswordHash(generatePassword(10));

        if (trainerRepository.selectTrainer(trainer.getUsername()).isEmpty()) {
            trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName());
            log.debug("Generated username: {}", trainer.getUsername());
        } else {
            int size = trainerRepository.selectByUsernameContaining(trainer.getUsername()).size();
            trainer.setUsername(size + trainer.getFirstName() + "." + trainer.getLastName());
            log.debug("Username already existed. New generated username: {}", trainer.getUsername());
        }

        trainerRepository.createTrainer(trainer);
        log.info("Trainer create with username: {}", trainer.getUsername());
    }

    public void updateTrainer(Trainer trainer) {
        log.info("Updating trainer with username: {}", trainer.getUsername());
        trainerRepository.updateTrainer(trainer);
        log.debug("Trainer updated: {}", trainer);
    }

    public Trainer selectTrainer(String username) throws NoSuchTrainerException {
        log.info("Fetching trainer with username: {}", username);
        return trainerRepository
                .selectTrainer(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new NoSuchTrainerException();
                });
    }

    public void activateTrainer(String username) throws NoSuchTrainerException {
        log.info("Activating trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.TRUE);
        updateTrainer(trainer);
        log.debug("Trainer {} activated", username);
    }

    public void deactivateTrainer(String username) throws NoSuchTrainerException {
        log.info("Deactivating trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.FALSE);
        updateTrainer(trainer);
        log.debug("Trainer {} deactivated", username);
    }

    public boolean validateTrainerCredentials(String username, String password) throws NoSuchTrainerException {
        log.info("Validating credentials for trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        boolean valid = trainer.getPasswordHash().equals(password);
        log.debug("Credentials valid: {}", valid);
        return valid;
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws NoSuchTrainerException {
        log.info("Changing password for trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);

        if (!trainer.getPasswordHash().equals(oldPassword)) {
            log.warn("Password change failed for {}: old password doesn't match", username);
            throw new IllegalArgumentException("Old password doesn't match");
        }

        trainer.setPasswordHash(newPassword);
        trainerRepository.updateTrainer(trainer);
        log.info("Password changed successfully for trainer: {}", username);
    }
}