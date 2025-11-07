package controller.v1;

import dto.request.CreateTrainingRequest;
import exceptions.NoSuchTraineeException;
import exceptions.NoSuchTrainerException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TraineeService;
import service.TrainingService;

@RequestMapping(value = "/api/v1/training", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "Training", description = "Operations related to trainings management")
public class TrainingControllerV1 {
    private final TrainingService trainingService;
    private final TraineeService traineeService;

    @PostMapping("/")
    public ResponseEntity<Void> createTraining(@Valid @RequestBody CreateTrainingRequest request,
                                               HttpServletRequest httpRequest) {
        log.info("Creating training: trainee={}, trainer={}, training name={}",
                request.getTraineeUsername(), request.getTrainerUsername(), request.getTrainingName());
        if (!traineeService.isUsernameSame(httpRequest, request.getTraineeUsername())) {
            log.warn("Training creation denied: username in token != traineeUsername ({})", request.getTraineeUsername());
            return ResponseEntity.badRequest().build();
        }

        try {
            trainingService.createTraining(request);
            log.info("Training successfully created: trainee={}, trainer={}, trainingName={}",
                    request.getTraineeUsername(), request.getTrainerUsername(), request.getTrainingName());
        } catch (NoSuchTraineeException | NoSuchTrainerException e) {
            log.warn("Training creation failed (missing user): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
