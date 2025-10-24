package service;

import dto.request.ChangeLoginRequest;
import dto.request.GetProfileRequest;
import dto.request.LoginRequest;
import dto.request.TraineeRegRequest;
import dto.request.UpdateTraineeProfileRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import dto.response.TraineeProfileResponse;
import entity.Trainee;
import entity.Trainer;
import entity.Training;
import exceptions.NoSuchTraineeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.TraineeMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.TraineeRepo;

import java.util.ArrayList;
import java.util.List;

import static utility.PasswordGenerator.generatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepo traineeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TraineeMapper mapper;

    public RegResponse createTrainee(TraineeRegRequest request) {
        Trainee trainee = mapper.toEntity(request);
        String password = generatePassword(10);
        trainee.setPasswordHash(passwordEncoder.encode(password));
        trainee.setUsername(generateUniqueUsername(trainee.getFirstName(), trainee.getLastName()));
        traineeRepository.createTrainee(trainee);
        log.info("Trainee created successfully with username={}", trainee.getUsername());
        return mapper.toRegResponse(trainee.getUsername(), password);
    }

    public JwtAuthResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Trainee trainee =  traineeRepository.selectTrainee(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(trainee);
        return new JwtAuthResponse(accessToken, jwtService.getAccessTokenExpiration());
    }

    public void updateTrainee(Trainee trainee) {
        log.info("Updating trainee username={}", trainee.getUsername());
        traineeRepository.updateTrainee(trainee);
    }

    public TraineeProfileResponse updateTraineeProfile(UpdateTraineeProfileRequest request) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(request.getUsername());
        updateTrainee(mapper.toUpdatedEntity(request, trainee));
        return mapper.toProfile(trainee);
    }

    public Trainee selectTrainee(String username) throws NoSuchTraineeException {
        log.debug("Selecting trainee by username={}", username);
        return traineeRepository
                .selectTrainee(username)
                .orElseThrow(() -> {
                    log.warn("No trainee found with username={}", username);
                    return new NoSuchTraineeException();
                });
    }

    public void activateTrainee(String username) throws NoSuchTraineeException {
        log.info("Activating trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.TRUE);
        updateTrainee(trainee);
        log.info("Trainee username={} activated", username);
    }

    public void deactivateTrainee(String username) throws NoSuchTraineeException {
        log.info("Deactivating trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        trainee.setIsActive(Boolean.FALSE);
        updateTrainee(trainee);
        log.info("Trainee username={}", username);
    }

    public void deleteTrainee(String username) {
        log.info("Deleting trainee={}", username);
        traineeRepository.deleteTrainee(username);
        log.info("Trainee username={} deleted", username);
    }

    public boolean validateTraineeCredentials(String username, String password) throws NoSuchTraineeException {
        log.debug("Validating credentials for trainee username={}", username);
        Trainee trainee = selectTrainee(username);
        boolean isValid = passwordEncoder.matches(password, trainee.getPasswordHash());
        log.info("Credentials validation for username={} result={}", username, isValid);
        return isValid;
    }

    public void changePassword(ChangeLoginRequest request) throws NoSuchTraineeException {
        log.info("Changing password for trainee username={}", request.getUsername());
        Trainee trainee = selectTrainee(request.getUsername());

        if (!validateTraineeCredentials(request.getUsername(), request.getOldPassword())) {
            log.debug("Password change failed: incorrect old password for username={}", request.getUsername());
            throw new IllegalArgumentException("Old password is incorrect");
        }

        trainee.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        traineeRepository.updateTrainee(trainee);
        log.info("Password changed successfully for trainee username={}", request.getUsername());
    }

    public boolean isUsernameSame(HttpServletRequest request, String username) {
        return jwtService.extractUsername(getToken(request)).equals(username);
    }

    public TraineeProfileResponse getTraineeProfile(GetProfileRequest request) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(request.getUsername());
        TraineeProfileResponse response = mapper.toProfile(trainee);
        response.setTrainerProfiles(getTrainerProfile(trainee.getUsername()));
        return response;
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        log.debug("Creating trainee with temporary username={}", baseUsername);
        String newUsername = baseUsername;
        int suffix = 1;
        while (traineeRepository.selectTrainee(newUsername).isPresent()) {
            newUsername = baseUsername + suffix++;
            log.debug("Username exists, generated new username={}", newUsername);
        }
        return newUsername;
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "No token found";
    }

    private List<TraineeProfileResponse.TrainerProfile> getTrainerProfile(String username) {
        List<Training> trainings = traineeRepository.selectTrainingsByUsername(username);
        List<TraineeProfileResponse.TrainerProfile> profiles = new ArrayList<>();
        for (Training t : trainings) {
            profiles.add(mapper.toTrainerProfile(t.getTrainer()));
        }

        return profiles;
    }

    public List<TraineeProfileResponse.TrainerProfile> getNotAssignedTrainers(String username) {
        List<Trainer> trainers = traineeRepository.selectTrainersNotAssignedByUsername(username);
        List<TraineeProfileResponse.TrainerProfile> trainerProfiles = new ArrayList<>();
        for (Trainer t : trainers) {
            trainerProfiles.add(mapper.toTrainerProfile(t));
        }
        return trainerProfiles;
    }
}
