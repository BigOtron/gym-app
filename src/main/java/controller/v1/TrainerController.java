package controller.v1;

import dto.request.LoginRequest;
import dto.request.TrainerRegRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TrainerService;

@RestController
@RequestMapping(value = "/api/v1/trainers", consumes = "application/json", produces = "application/json")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@RequestBody TrainerRegRequest trainerRegRequest) {
        RegResponse response = trainerService.createTrainer(trainerRegRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request) {
        JwtAuthResponse response = trainerService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
