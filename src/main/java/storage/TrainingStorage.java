package storage;

import entity.Training;
import entity.TrainingType;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TrainingStorage is a component responsible for loading and storing {@link Training} entities.
 * <p>
 * Each Training represents a session conducted by a trainer for a trainee, including information
 * such as training type, time, and duration.
 * </p>
 * <p>
 * The storage is internally represented as a {@link Map} with the training ID as the key
 * and the corresponding {@link Training} object as the value.
 * </p>
 * <p>
 * This class reads training data from an external CSV file during application initialization.
 * Each line in the CSV file should represent a training record with comma-separated values:
 * <ul>
 *     <li>Training ID</li>
 *     <li>Trainee ID</li>
 *     <li>Trainer ID</li>
 *     <li>Training Name</li>
 *     <li>Training Type (e.g., CARDIO, RUNNING)</li>
 *     <li>Training Time (formatted as yyyy-MM-dd H:mm)</li>
 *     <li>Duration in minutes</li>
 * </ul>
 * </p>
 */
@Component
@Getter
@RequiredArgsConstructor
public class TrainingStorage {
    private final Map<Long, Training> storage = new HashMap<>();
    private final static Logger logger  = LoggerFactory.getLogger(TrainingStorage.class);

    @Value("${storage.file.path.training}")
    private String filePath;

    @PostConstruct
    public void init() throws IOException {
        logger.info("Initializing TrainingStorage from file: {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Reading line: {}", line);
                String[] lineData = line.split(",");
                Training training = parseTraining(lineData);
                storage.put(training.getTrainingId(), training);
            }
        }
    }

    private Training parseTraining(String[] lineData) {
        if (lineData.length < 7) {
            throw new IllegalArgumentException(
                    "Invalid training record: " + Arrays.toString(lineData));
        }
        Training training = new Training();
        training.setTrainingId(Long.parseLong(lineData[0]));
        training.setTraineeId(Long.parseLong(lineData[1]));
        training.setTrainerId(Long.parseLong(lineData[2]));
        training.setTrainingName(lineData[3]);
        training.setTrainingType(TrainingType.valueOf(lineData[4].toUpperCase()));
        training.setTrainingTime(parseTrainingTime(lineData[5]));
        training.setDuration(Duration.ofMinutes(Long.parseLong(lineData[6])));

        return training;
    }

    private LocalDateTime parseTrainingTime(String dateAndTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        return LocalDateTime.parse(dateAndTime, formatter);
    }
}
