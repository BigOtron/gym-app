package repository;

import entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepo {
    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> selectTrainer(String username);
    List<Trainer> selectByUsernameContaining(String username);
}
