package service;

import entity.Training;
import exceptions.NoSuchTrainingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.TrainingRepo;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepo trainingRepository;

    public void createTraining(Training training) {
        trainingRepository.createTraining(training);
    }

    public Training selectTraining(UUID id) throws NoSuchTrainingException {
        return trainingRepository
                .selectTraining(id)
                .orElseThrow(NoSuchTrainingException::new);
    }
}
