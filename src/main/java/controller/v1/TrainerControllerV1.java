package controller.v1;

import dto.request.GetProfileRequest;
import dto.request.LoginRequest;
import dto.request.SetStatusRequest;
import dto.request.TrainerRegRequest;
import dto.request.TrainerTrainingsRequest;
import dto.request.UpdateTrainerProfileRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import dto.response.TrainerProfileResponse;
import dto.response.TrainerTrainingsResponse;
import exceptions.NoSuchTrainerException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TrainerService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainers", consumes = "application/json", produces = "application/json")
@Tag(name = "Trainers", description = "Operations related to trainer accounts and profiles")
public class TrainerControllerV1 {
    private final TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@Valid @RequestBody TrainerRegRequest trainerRegRequest) {
        log.info("Received trainer registration request for name: {}", trainerRegRequest.getFirstName());
        RegResponse response = trainerService.createTrainer(trainerRegRequest);
        log.info("Trainer registered successfully: {}", response.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Trainer login attempt for username: {}", request.getUsername());
        JwtAuthResponse response = trainerService.authenticate(request);
        log.info("Trainer login successful for username: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<TrainerProfileResponse> getProfile(@Valid @RequestBody GetProfileRequest request,
                                                             HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Profile request for trainer username: {}", username);

        if (!trainerService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized profile access attempt by token user for trainer: {}", username);
            return ResponseEntity.badRequest().build();
        }

        try {
            TrainerProfileResponse profile = trainerService.getTrainerProfile(request);
            log.info("Trainer profile retrieved successfully: {}", username);
            return ResponseEntity.ok(profile);
        } catch (NoSuchTrainerException e) {
            log.warn("Trainer not found while retrieving profile: {}", username);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<TrainerProfileResponse> updateProfile(@Valid @RequestBody UpdateTrainerProfileRequest request,
                                                                HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Update trainer profile request for username: {}", username);

        if (!trainerService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized profile update attempt by token user for trainer: {}", username);
            return ResponseEntity.badRequest().build();
        }

        try {
            TrainerProfileResponse response = trainerService.updateTrainerProfile(request);
            log.info("Trainer profile updated successfully: {}", username);
            return ResponseEntity.ok(response);
        } catch (NoSuchTrainerException e) {
            log.warn("Trainer not found while updating profile: {}", username);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<TrainerTrainingsResponse>> getTrainerTrainings(@Valid @RequestBody TrainerTrainingsRequest request,
                                                                              HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Fetching trainings for trainer username: {}", username);

        if (!trainerService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized access attempt to trainings by token user for trainer: {}", username);
            return ResponseEntity.badRequest().build();
        }

        List<TrainerTrainingsResponse> trainings = trainerService.getTrainerTrainings(request);
        log.info("Retrieved {} trainings for trainer: {}", trainings.size(), username);
        return ResponseEntity.ok(trainings);
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> setStatus(@Valid @RequestBody SetStatusRequest request,
                                          HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Set trainer status request for username: {}", username);

        if (!trainerService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized status change attempt by token user for trainer: {}", username);
            return ResponseEntity.badRequest().build();
        }

        try {
            trainerService.changeStatus(request);
            log.info("Trainer status updated successfully: {}", username);
        } catch (NoSuchTrainerException e) {
            log.warn("Trainer not found while changing status: {}", username);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
