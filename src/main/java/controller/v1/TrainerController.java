package controller.v1;

import dto.request.GetProfileRequest;
import dto.request.LoginRequest;
import dto.request.TrainerRegRequest;
import dto.request.UpdateTraineeProfileRequest;
import dto.request.UpdateTrainerProfileRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import dto.response.TraineeProfileResponse;
import dto.response.TrainerProfileResponse;
import exceptions.NoSuchTraineeException;
import exceptions.NoSuchTrainerException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/me")
    public ResponseEntity<TrainerProfileResponse> getProfile(@RequestBody GetProfileRequest request,
                                                             HttpServletRequest httpRequest) {
        // check if username and token username match
        if (!trainerService.isUsernameSame(httpRequest, request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            TrainerProfileResponse profile = trainerService.getTrainerProfile(request);
            return ResponseEntity.ok(profile);
        } catch (NoSuchTrainerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<TrainerProfileResponse> updateProfile(@RequestBody UpdateTrainerProfileRequest request,
                                                                HttpServletRequest httpRequest) {
        // check if username and token username match
        if (!trainerService.isUsernameSame(httpRequest, request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        TrainerProfileResponse response = null;
        try {
            response = trainerService.updateTrainerProfile(request);
        } catch (NoSuchTrainerException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
