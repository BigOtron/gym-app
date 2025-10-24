package controller.v1;

import dto.request.CreateTrainingRequest;
import exceptions.NoSuchTraineeException;
import exceptions.NoSuchTrainerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;

    @PostMapping("/create")
    public ResponseEntity<Void> createTraining(@RequestBody CreateTrainingRequest request,
                                               HttpServletRequest httpRequest) {
        System.out.println(request.getTraineeUsername());
        if (!traineeService.isUsernameSame(httpRequest, request.getTraineeUsername())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            trainingService.createTraining(request);
        } catch (NoSuchTraineeException | NoSuchTrainerException e) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
