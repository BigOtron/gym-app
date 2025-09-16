package service;

import dao.TrainerDAO;
import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private final TrainerDAO trainerDAO;

    public TrainerService(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public void createTrainer(Trainer trainer) {
        trainerDAO.createTrainer(trainer);
    }

    public void updateTrainer(Trainer trainer) {
        try {
            trainerDAO.updateTrainer(trainer);
        } catch (NoSuchTrainerException e) {
            // we will inform the caller about this
            throw new RuntimeException(e);
        }
    }

    public Trainer selectTrainer(long id) {
        try {
            return trainerDAO.selectTrainer(id);
        } catch (NoSuchTrainerException e) {
            // we will inform the caller about this
            throw new RuntimeException(e);
        }
    }
}
