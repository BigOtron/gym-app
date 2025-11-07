package gymapp.controller.v1;

import gymapp.dto.request.ChangeLoginRequest;
import gymapp.dto.request.DeleteTraineeRequest;
import gymapp.dto.request.GetProfileRequest;
import gymapp.dto.request.LoginRequest;
import gymapp.dto.request.SetStatusRequest;
import gymapp.dto.request.UpdateTraineeProfileRequest;
import gymapp.dto.response.TraineeProfileResponse;
import gymapp.dto.request.TraineeRegRequest;
import gymapp.dto.response.JwtAuthResponse;
import gymapp.dto.response.RegResponse;

import gymapp.dto.response.TraineeTrainingsRequest;
import gymapp.dto.response.TraineeTrainingsResponse;
import gymapp.exceptions.NoSuchTraineeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gymapp.service.TraineeService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/trainees", consumes = "application/json", produces = "application/json")
@Tag(name = "Trainees", description = "Operations related to trainee accounts and profiles")
public class TraineeControllerV1 {
    private final TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@Valid @RequestBody TraineeRegRequest traineeRegRequest) {
        log.debug("Received registration request for user: {} {}", traineeRegRequest.getFirstName(), traineeRegRequest.getLastName());
        RegResponse response =traineeService.createTrainee(traineeRegRequest);
        log.debug("Trainee registered successfully: {}", response.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.debug("Login attempt for username: {}", request.getUsername());
        JwtAuthResponse response = traineeService.authenticate(request);
        log.debug("Login successful for username: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changeLogin(@Valid @RequestBody ChangeLoginRequest request) throws NoSuchTraineeException {
        log.info("Change password request for username: {}", request.getUsername());
        traineeService.changePassword(request);
        log.info("Password changed successfully for username: {}", request.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<TraineeProfileResponse> getProfile(@Valid @RequestBody GetProfileRequest request,
                                                             HttpServletRequest httpRequest) throws NoSuchTraineeException{
        String username = request.getUsername();
        log.info("Profile request for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized profile access attempt by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }


        TraineeProfileResponse profile = traineeService.getTraineeProfile(request);
        log.info("Profile retrieved successfully for username: {}", username);
        return ResponseEntity.ok(profile);

    }

    @PutMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> updateProfile(@Valid @RequestBody UpdateTraineeProfileRequest request,
                                                                HttpServletRequest httpRequest) throws NoSuchTraineeException{
        String username = request.getUsername();
        log.info("Update profile request for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized update attempt by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }


        TraineeProfileResponse response = traineeService.updateTraineeProfile(request);
        log.info("Profile updated successfully for username: {}", username);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(@Valid @RequestBody DeleteTraineeRequest request,
                                              HttpServletRequest httpRequest) throws NoSuchTraineeException {
        String username = request.getUsername();
        log.info("Delete trainee request for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized delete attempt by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }

        traineeService.deleteTrainee(username);
        log.info("Trainee deleted successfully: {}", username);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/not-assigned-trainers")
    public ResponseEntity<List<TraineeProfileResponse.TrainerProfile>> getNotAssignedTrainers(@Valid @RequestBody GetProfileRequest request,
                                                                                        HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Fetching not-assigned trainers for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized request for not-assigned trainers by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }

        List<TraineeProfileResponse.TrainerProfile> trainers = traineeService.getNotAssignedTrainers(username);
        log.info("Retrieved {} unassigned trainers for username: {}", trainers.size(), username);
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<TraineeTrainingsResponse>> getTraineeTrainings(@Valid @RequestBody TraineeTrainingsRequest request,
                                                                        HttpServletRequest httpRequest) {
        String username = request.getUsername();
        log.info("Fetching trainings for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized training access attempt by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }

        List<TraineeTrainingsResponse> trainings = traineeService.getTraineeTrainings(request);
        log.info("Retrieved {} trainings for username: {}", trainings.size(), username);
        return ResponseEntity.ok(trainings);
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> setStatus(@Valid @RequestBody SetStatusRequest request,
                                          HttpServletRequest httpRequest) throws NoSuchTraineeException{
        String username = request.getUsername();
        log.info("Set status request for username: {}", username);

        if (!traineeService.isUsernameSame(httpRequest, username)) {
            log.warn("Unauthorized status change attempt by token username: {}", username);
            return ResponseEntity.badRequest().build();
        }

        traineeService.changeStatus(request);
        log.info("Status updated successfully for username: {}", username);
        return ResponseEntity.ok().build();
    }
}
