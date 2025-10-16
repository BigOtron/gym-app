package controller.v1;

import dto.request.TraineeRegRequest;
import dto.response.RegResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TraineeService;

@RestController
@RequestMapping(value = "/api/v1/trainees", consumes = "application/json", produces = "application/json")
public class TraineeController {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@RequestBody TraineeRegRequest traineeRegRequest) {
        RegResponse response =traineeService.createTrainee(traineeRegRequest);
        return ResponseEntity.ok(response);
    }
}
