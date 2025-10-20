package controller.v1;

import dto.request.LoginRequest;
import dto.request.TraineeRegRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TraineeService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/trainees", consumes = "application/json", produces = "application/json")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@RequestBody TraineeRegRequest traineeRegRequest) {
        RegResponse response =traineeService.createTrainee(traineeRegRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request) {
        JwtAuthResponse response = traineeService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
