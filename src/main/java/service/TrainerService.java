package service;

import entity.Trainee;
import entity.Trainer;
import exceptions.NoSuchTraineeException;
import exceptions.NoSuchTrainerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.TrainerRepo;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepo trainerRepository;

    public void createTrainer(Trainer trainer) {
        trainerRepository.createTrainer(trainer);
    }

    public void updateTrainer(Trainer trainer) {
        trainerRepository.updateTrainer(trainer);
    }

    public Trainer selectTrainer(String username) throws NoSuchTrainerException {
        return trainerRepository
                .selectTrainer(username)
                .orElseThrow(NoSuchTrainerException::new);
    }

    public void activateTrainer(String username) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.TRUE);
        updateTrainer(trainer);
    }

    public void deactivateTrainer(String username) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.FALSE);
        updateTrainer(trainer);
    }

    public boolean validateTrainerCredentials(String username, String password) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(username);
        return trainer.getPasswordHash().equals(password);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(username);
        if (!trainer.getPasswordHash().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password doesn't match");
        }
        trainer.setPasswordHash(newPassword);
        trainerRepository.updateTrainer(trainer);
    }
}
