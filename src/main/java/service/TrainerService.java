package service;

import entity.Trainer;
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
}
