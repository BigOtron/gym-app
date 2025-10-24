package service;

import dto.request.CreateTrainingRequest;
import entity.Trainee;
import entity.Trainer;
import entity.Training;
import exceptions.NoSuchTraineeException;
import exceptions.NoSuchTrainerException;
import exceptions.NoSuchTrainingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.TrainingMapper;
import org.springframework.stereotype.Service;
import repository.TrainingRepo;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingService {
    private final TrainingRepo trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingMapper mapper;

    public void createTraining(CreateTrainingRequest request) throws NoSuchTraineeException, NoSuchTrainerException {
        Trainee trainee = traineeService.selectTrainee(request.getTraineeUsername());
        Trainer trainer = trainerService.selectTrainer(request.getTrainerUsername());
        Training training = mapper.toTraining(request);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());
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