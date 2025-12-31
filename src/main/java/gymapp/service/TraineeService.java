package gymapp.service;

import gymapp.dto.request.ChangeLoginRequest;
import gymapp.dto.request.GetProfileRequest;
import gymapp.dto.request.LoginRequest;
import gymapp.dto.request.SetStatusRequest;
import gymapp.dto.request.TraineeRegRequest;
import gymapp.dto.request.UpdateTraineeProfileRequest;
import gymapp.dto.response.JwtAuthResponse;
import gymapp.dto.response.RegResponse;
import gymapp.dto.response.TraineeProfileResponse;
import gymapp.dto.response.TraineeTrainingsRequest;
import gymapp.dto.response.TraineeTrainingsResponse;
import gymapp.entity.Trainee;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
import gymapp.exceptions.NoSuchTraineeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import gymapp.mapper.TraineeMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import gymapp.repository.TraineeRepository;

import java.util.ArrayList;
import java.util.List;

import static gymapp.utility.PasswordGenerator.generatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TraineeMapper mapper;

    public RegResponse createTrainee(TraineeRegRequest request) {
        Trainee trainee = mapper.toEntity(request);
        String password = generatePassword(10);
        trainee.setPasswordHash(passwordEncoder.encode(password));
        trainee.setUsername(generateUniqueUsername(trainee.getFirstName(), trainee.getLastName()));
        traineeRepository.save(trainee);
        log.info("Trainee created successfully with username={}", trainee.getUsername());
        return mapper.toRegResponse(trainee.getUsername(), password);
    }

    public JwtAuthResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user with username={}", loginRequest.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        log.debug("successfully authenticated username={}", loginRequest.getUsername());

        Trainee trainee =  traineeRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.error("Trainee not found for username={}", loginRequest.getUsername());
                    return new UsernameNotFoundException("User not found");
                });
        log.debug("Trainee found: username={}", trainee.getUsername());

        String accessToken = jwtService.generateAccessToken(trainee);
        log.info("Generated access token for username={}", trainee.getUsername());
        return new JwtAuthResponse(accessToken, jwtService.getAccessTokenExpiration());
    }

    public void updateTrainee(Trainee trainee) {
        log.info("Updating trainee username={}", trainee.getUsername());
        traineeRepository.save(trainee);
    }

    public TraineeProfileResponse updateTraineeProfile(UpdateTraineeProfileRequest request) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(request.getUsername());
        updateTrainee(mapper.toUpdatedEntity(request, trainee));
        log.info("Trainee profile update successful for username={}", request.getUsername());
        return mapper.toProfile(trainee);
    }

    public Trainee selectTrainee(String username) throws NoSuchTraineeException {
        log.debug("Selecting trainee by username={}", username);
        return traineeRepository
                .findByUsername(username)
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
        log.info("Trainee username={} deactivated", username);
    }

    public void deleteTrainee(String username) throws NoSuchTraineeException {
        log.info("Deleting trainee={}", username);
        traineeRepository.delete(traineeRepository.findByUsername(username).orElseThrow(NoSuchTraineeException::new));
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
        traineeRepository.save(trainee);
        log.info("Password changed successfully for trainee username={}", request.getUsername());
    }

    public boolean isUsernameSame(HttpServletRequest request, String username) {
        log.debug("Checking if username={} is the same as the username in the request token", username);
        return jwtService.extractUsername(getToken(request)).equals(username);
    }

    public TraineeProfileResponse getTraineeProfile(GetProfileRequest request) throws NoSuchTraineeException {
        Trainee trainee = selectTrainee(request.getUsername());
        TraineeProfileResponse response = mapper.toProfile(trainee);
        response.setTrainerProfiles(getTrainerProfile(trainee.getUsername()));
        log.info("Trainee profile fetched successfully for username={}", request.getUsername());
        return response;
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        log.debug("Creating trainee with temporary username={}", baseUsername);
        String newUsername = baseUsername;
        int suffix = 1;
        while (traineeRepository.findByUsername(newUsername).isPresent()) {
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
        List<Training> trainings = traineeRepository.findTrainingsByTraineeUsername(username);
        List<TraineeProfileResponse.TrainerProfile> profiles = new ArrayList<>();
        for (Training t : trainings) {
            profiles.add(mapper.toTrainerProfile(t.getTrainer()));
        }

        return profiles;
    }

    public List<TraineeProfileResponse.TrainerProfile> getNotAssignedTrainers(String username) {
        List<Trainer> trainers = traineeRepository.findTrainersNotAssignedToTrainee(username);
        List<TraineeProfileResponse.TrainerProfile> trainerProfiles = new ArrayList<>();
        for (Trainer t : trainers) {
            trainerProfiles.add(mapper.toTrainerProfile(t));
        }
        log.info("Successfully mapped {} unassigned trainers for username={}", trainerProfiles.size(), username);
        return trainerProfiles;
    }

    public List<TraineeTrainingsResponse> getTraineeTrainings(TraineeTrainingsRequest request) {
        List<Training> trainings = traineeRepository.findTrainingsByTraineeUsername(request.getUsername());
        List<TraineeTrainingsResponse> responseList = new ArrayList<>();
        for (Training t : trainings) {
            responseList.add(mapper.toTraining(t));
        }
        log.info("Successfully mapped {} trainings for username={}", responseList.size(), request.getUsername());
        return responseList;
    }

    public void changeStatus(SetStatusRequest request) throws NoSuchTraineeException {
        if (request.isActive()) {
            activateTrainee(request.getUsername());
        } else {
            deactivateTrainee(request.getUsername());
        }
    }
}
