package gymapp.service;

import gymapp.dto.request.GetProfileRequest;
import gymapp.dto.request.LoginRequest;
import gymapp.dto.request.SetStatusRequest;
import gymapp.dto.request.TrainerRegRequest;
import gymapp.dto.request.TrainerTrainingsRequest;
import gymapp.dto.request.UpdateTrainerProfileRequest;
import gymapp.dto.response.JwtAuthResponse;
import gymapp.dto.response.RegResponse;
import gymapp.dto.response.TrainerProfileResponse;
import gymapp.dto.response.TrainerTrainingsResponse;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
import gymapp.exceptions.NoSuchTrainerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import gymapp.mapper.TrainerMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import gymapp.repository.TrainerRepository;
import gymapp.repository.TrainingTypeRepository;

import java.util.ArrayList;
import java.util.List;

import static gymapp.utility.PasswordGenerator.generatePassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TrainerMapper trainerMapper;

    public RegResponse createTrainer(TrainerRegRequest request) {
        Trainer trainer = trainerMapper.toEntity(request);
        trainingTypeRepo.save(trainer.getSpecialization());
        String password = generatePassword(10);
        trainer.setPasswordHash(passwordEncoder.encode(password));
        trainer.setUsername(generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainerRepository.save(trainer);
        log.info("Trainer create with username: {}", trainer.getUsername());
        return trainerMapper.toRegResponse(trainer.getUsername(), password);
    }

    public JwtAuthResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating trainer user {}", loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Trainer trainer =  trainerRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Trainer user not found"));

        String accessToken = jwtService.generateAccessToken(trainer);
        return new JwtAuthResponse(accessToken, jwtService.getAccessTokenExpiration());
    }

    public void updateTrainer(Trainer trainer) {
        log.info("Updating trainer with username: {}", trainer.getUsername());
        trainerRepository.save(trainer);
        log.debug("Trainer updated: {}", trainer);
    }

    public Trainer selectTrainer(String username) throws NoSuchTrainerException {
        log.info("Fetching trainer with username: {}", username);
        return trainerRepository
                .findByUsername(username)
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
        trainerRepository.save(trainer);
        log.info("Password changed successfully for trainer: {}", username);
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        log.debug("Creating trainer with temporary username={}", baseUsername);
        String newUsername = baseUsername;
        int suffix = 1;
        while (trainerRepository.findByUsername(newUsername).isPresent()) {
            newUsername = baseUsername + suffix++;
            log.debug("Username exists, generated new username={}", newUsername);
        }
        return newUsername;
    }

    public TrainerProfileResponse getTrainerProfile(GetProfileRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        TrainerProfileResponse response = trainerMapper.toProfile(trainer);
        response.setTraineeProfiles(getTraineeProfiles(trainer.getUsername()));
        log.debug("Fetched trainer profile successfully");
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
        List<Training> trainings = trainerRepository.findTrainingsByTrainerUsername(username);
        List<TrainerProfileResponse.TraineeProfile> profiles = new ArrayList<>();
        for (Training t : trainings) {
            profiles.add(trainerMapper.toTraineeProfile(t.getTrainee()));
        }
        return profiles;
    }

    public TrainerProfileResponse updateTrainerProfile(UpdateTrainerProfileRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        updateTrainer(trainerMapper.toUpdatedEntity(request, trainer));
        log.debug("Updated trainer profile with username={} successfully", request.getUsername());
        return trainerMapper.toProfile(trainer);
    }

    public List<TrainerTrainingsResponse> getTrainerTrainings(TrainerTrainingsRequest request) {
        List<Training> trainings = trainerRepository.findTrainingsByTrainerUsername(request.getUsername());
        List<TrainerTrainingsResponse> responseList = new ArrayList<>();
        for (Training t : trainings) {
            responseList.add(trainerMapper.toTrainings(t));
        }
        log.debug("Fetched trainings for trainer username={} successfully", request.getUsername());
        return responseList;
    }

    public void changeStatus(SetStatusRequest request) throws NoSuchTrainerException {
        Trainer trainer = selectTrainer(request.getUsername());
        trainer.setIsActive(request.isActive());
        updateTrainer(trainer);
    }
}