package service;

import dto.request.LoginRequest;
import dto.request.TrainerRegRequest;
import dto.response.JwtAuthResponse;
import dto.response.RegResponse;
import entity.Trainee;
import entity.Trainer;
import entity.TrainingType;
import exceptions.NoSuchTrainerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.TrainerRepo;
import repository.TrainingTypeRepo;

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

    public RegResponse createTrainer(TrainerRegRequest request) {
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization(request.getSpecialization());
        trainingTypeRepo.createTrainingType(trainingType);
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setSpecialization(trainingType);
        String password = generatePassword(10);
        trainer.setPasswordHash(passwordEncoder.encode(password));
        trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName());
        log.info("Creating trainer: {} {}", trainer.getFirstName(), trainer.getLastName());

        if (trainerRepository.selectTrainer(trainer.getUsername()).isPresent()) {
            int size = trainerRepository.selectByUsernameContaining(trainer.getUsername()).size();
            trainer.setUsername(size + trainer.getFirstName() + "." + trainer.getLastName());
            log.debug("Username already existed. New generated username: {}", trainer.getUsername());
        }

        trainerRepository.createTrainer(trainer);
        log.info("Trainer create with username: {}", trainer.getUsername());
        return new RegResponse(trainer.getUsername(), password);
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
}