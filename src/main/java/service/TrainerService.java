package service;

import dto.request.GetProfileRequest;
import dto.request.LoginRequest;
import dto.request.SetStatusRequest;
import dto.request.TrainerRegRequest;
import dto.request.TrainerTrainingsRequest;
import dto.request.UpdateTrainerProfileRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import dto.response.TraineeTrainingsRequest;
import dto.response.TraineeTrainingsResponse;
import dto.response.TrainerProfileResponse;
import dto.response.TrainerTrainingsResponse;
import entity.Trainer;
import entity.Training;
import exceptions.NoSuchTrainerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.TrainerMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.TrainerRepo;
import repository.TrainingTypeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utility.PasswordGenerator.generatePassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepo trainerRepository;
    private final TrainingTypeRepo trainingTypeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TrainerMapper trainerMapper;

    public RegResponse createTrainer(TrainerRegRequest request) {
        Trainer trainer = trainerMapper.toEntity(request);
        trainingTypeRepo.createTrainingType(trainer.getSpecialization());
        String password = generatePassword(10);
        trainer.setPasswordHash(passwordEncoder.encode(password));
        trainer.setUsername(generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainerRepository.createTrainer(trainer);
        log.info("Trainer create with username: {}", trainer.getUsername());
        return trainerMapper.toRegResponse(trainer.getUsername(), password);
    }

    public JwtAuthResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user {}", loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Trainer trainer =  trainerRepository.selectTrainer(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Trainer user not found"));

        String accessToken = jwtService.generateAccessToken(trainer);
        return new JwtAuthResponse(accessToken, jwtService.getAccessTokenExpiration());
    }

    public void updateTrainer(Trainer trainer) {
        log.info("Updating trainer with username: {}", trainer.getUsername());
        trainerRepository.updateTrainer(trainer);
        log.debug("Trainer updated: {}", trainer);
    }

    public Trainer selectTrainer(String username) throws NoSuchTrainerException {
        log.info("Fetching trainer with username: {}", username);
        return trainerRepository
                .selectTrainer(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new NoSuchTrainerException();
                });
    }

    public void activateTrainer(String username) throws NoSuchTrainerException {
        log.info("Activating trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.TRUE);
        updateTrainer(trainer);
        log.debug("Trainer {} activated", username);
    }

    public void deactivateTrainer(String username) throws NoSuchTrainerException {
        log.info("Deactivating trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        trainer.setIsActive(Boolean.FALSE);
        updateTrainer(trainer);
        log.debug("Trainer {} deactivated", username);
    }

    public boolean validateTrainerCredentials(String username, String password) throws NoSuchTrainerException {
        log.info("Validating credentials for trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);
        boolean valid = trainer.getPasswordHash().equals(password);
        log.debug("Credentials valid: {}", valid);
        return valid;
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws NoSuchTrainerException {
        log.info("Changing password for trainer with username: {}", username);
        Trainer trainer = selectTrainer(username);

        if (!trainer.getPasswordHash().equals(oldPassword)) {
            log.warn("Password change failed for {}: old password doesn't match", username);
            throw new IllegalArgumentException("Old password doesn't match");
        }

        trainer.setPasswordHash(newPassword);
        trainerRepository.updateTrainer(trainer);
        log.info("Password changed successfully for trainer: {}", username);
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        log.debug("Creating trainer with temporary username={}", baseUsername);
        String newUsername = baseUsername;
        int suffix = 1;
        while (trainerRepository.selectTrainer(newUsername).isPresent()) {
            newUsername = baseUsername + suffix++;
            log.debug("Username exists, generated new username={}", newUsername);
        }
        return newUsername;
    }

    public TrainerProfileResponse getTrainerProfile(GetProfileRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        TrainerProfileResponse response = trainerMapper.toProfile(trainer);
        response.setTraineeProfiles(getTraineeProfiles(trainer.getUsername()));
        return response;
    }

    public boolean isUsernameSame(HttpServletRequest request, String username) {
        return jwtService.extractUsername(getToken(request)).equals(username);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "No token found";
    }

    private List<TrainerProfileResponse.TraineeProfile> getTraineeProfiles(String username) {
        List<Training> trainings = trainerRepository.selectTrainingsByUsername(username);
        List<TrainerProfileResponse.TraineeProfile> profiles = new ArrayList<>();
        for (Training t : trainings) {
            profiles.add(trainerMapper.toTraineeProfile(t.getTrainee()));
        }
        return profiles;
    }

    public TrainerProfileResponse updateTrainerProfile(UpdateTrainerProfileRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        updateTrainer(trainerMapper.toUpdatedEntity(request, trainer));
        return trainerMapper.toProfile(trainer);
    }

    public List<TrainerTrainingsResponse> getTrainerTrainings(TrainerTrainingsRequest request) {
        List<Training> trainings = trainerRepository.selectTrainingsByUsername(request.getUsername());
        List<TrainerTrainingsResponse> responseList = new ArrayList<>();
        for (Training t : trainings) {
            responseList.add(trainerMapper.toTrainings(t));
        }

        return responseList;
    }

    public void changeStatus(SetStatusRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        trainer.setIsActive(request.isActive());
        updateTrainer(trainer);
    }
}