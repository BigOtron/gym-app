package service;

import entity.Training;
import exceptions.NoSuchTrainingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.TrainingRepo;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingService {
    private final TrainingRepo trainingRepository;

    public void createTraining(Training training) {
        log.info("Creating training: {} on {}", training.getTrainingName(), training.getTrainingDate());
        trainingRepository.createTraining(training);
        log.info("Training created successfully with ID: {}", training.getId());
    }

    public Training selectTraining(UUID id) throws NoSuchTrainingException {
        log.info("Fetching training with ID: {}", id);
        return trainingRepository
                .selectTraining(id)
                .orElseThrow(() -> {
                    log.warn("Training not found with ID: {}", id);
                    return new NoSuchTrainingException();
                });
    }
}