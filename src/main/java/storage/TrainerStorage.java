package storage;

import entity.Trainer;
import entity.TrainingType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utility.PasswordGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * TrainerStorage is a component responsible for
 * loading and storing Trainer entities from an external CSV file.
 * <p>
 * Each Trainer has a unique user ID and may have multiple specializations.
 * A password is generated for each Trainer using {@link PasswordGenerator}.
 * </p>
 * <p>
 * The storage is represented internally as a {@link Map} with the trainer's user ID as the key
 * and the corresponding {@link Trainer} object as the value.
 * Each line in the CSV file should represent a training record with comma-separated values:
 * <ul>
 *      <li>Trainer ID</li>
 *      <li>Trainer first name</li>
 *      <li>Trainer last name</li>
 *      <li>Trainer Specialization list (e.g., CARDIO, RUNNING)</li>
 *  </ul>
 * </p>
 */
@Component(value = "trainerStorage")
public class TrainerStorage {
    private final Map<Long, Trainer> storage = new HashMap<>();
    private final PasswordGenerator passwordGenerator;
    private final static Logger logger  = LoggerFactory.getLogger(TrainerStorage.class);

    @Value("${storage.file.path.trainer}")
    private String filePath;

    public TrainerStorage(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    public Map<Long, Trainer> getStorage() {
        return storage;
    }

    @PostConstruct
    public void init() throws IOException {
        logger.info("Initializing TrainerStorage from file: {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Reading line: {}", line);
                String[] lineData = line.split(",");
                Trainer trainer = parseTrainer(lineData);
                storage.put(trainer.getUserId(), trainer);
            }
        }
    }

    private Trainer parseTrainer(String[] data) {
        Trainer trainer = new Trainer();
        trainer.setUserId(Long.parseLong(data[0]));
        trainer.setFirstName(data[1]);
        trainer.setLastName(data[2]);
        trainer.setUsername(data[1] + "." + data[2]);
        trainer.setPassword(passwordGenerator.generatePassword(10));
        trainer.setSpecialization(parseSpecializations(data));

        return trainer;
    }

    private List<TrainingType> parseSpecializations(String[] data) {
        List<TrainingType> trainingTypes = new ArrayList<>();
        for (int i = 3; i < data.length; i++) {
            switch (data[i]) {
                case "cardio":
                    trainingTypes.add(TrainingType.CARDIO);
                    break;
                case "running":
                    trainingTypes.add(TrainingType.RUNNING);
                    break;
                case "weightlifting":
                    trainingTypes.add(TrainingType.WEIGHTLIFTING);
                    break;
                case "pull_up":
                    trainingTypes.add(TrainingType.PULL_UP);
                    break;
                case "push_up":
                    trainingTypes.add(TrainingType.PUSH_UP);
                    break;
            }
        }

        return trainingTypes;
    }
}
