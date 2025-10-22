package controller.v1;

import dto.request.ChangeLoginRequest;
import dto.request.GetProfileRequest;
import dto.request.LoginRequest;
import dto.request.UpdateTraineeProfileRequest;
import dto.response.TraineeProfileResponse;
import dto.request.TraineeRegRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import exceptions.NoSuchTraineeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(@RequestBody ChangeLoginRequest request) {
        try {
            traineeService.changePassword(request);
            return ResponseEntity.ok().build();
        } catch (NoSuchTraineeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<TraineeProfileResponse> getProfile(@RequestBody GetProfileRequest request,
                                                             HttpServletRequest httpRequest) {
        // check if username and token username match
        if (!traineeService.isUsernameSame(httpRequest, request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            TraineeProfileResponse profile = traineeService.getTraineeProfile(request);
            return ResponseEntity.ok(profile);
        } catch (NoSuchTraineeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<TraineeProfileResponse> updateProfile(@RequestBody UpdateTraineeProfileRequest request,
                                                                HttpServletRequest httpRequest) {
        // check if username and token username match
        if (!traineeService.isUsernameSame(httpRequest, request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        TraineeProfileResponse response = null;
        try {
            response = traineeService.updateTraineeProfile(request);
        } catch (NoSuchTraineeException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
