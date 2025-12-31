package gymapp.service;

import gymapp.dto.request.CreateTrainingRequest;
import gymapp.entity.Trainee;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
import gymapp.exceptions.NoSuchTraineeException;
import gymapp.exceptions.NoSuchTrainerException;
import gymapp.exceptions.NoSuchTrainingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import gymapp.mapper.TrainingMapper;
import org.springframework.stereotype.Service;
import gymapp.repository.TrainingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
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
        trainingRepository.save(training);
        log.info("Training created successfully with ID: {}", training.getId());
    }

    public Training selectTraining(UUID id) throws NoSuchTrainingException {
        log.info("Fetching training with ID: {}", id);
        return trainingRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("Training not found with ID: {}", id);
                    return new NoSuchTrainingException();
                });
    }
}