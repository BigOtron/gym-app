package storage;

import entity.Training;
import entity.TrainingType;
import jakarta.annotation.PostConstruct;
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

@Component
public class TrainingStorage {
    private final Map<Long, Training> storage = new HashMap<>();

    @Value("${storage.file.path.training}")
    private String filePath;

    public Map<Long, Training> getStorage() {
        return storage;
    }

    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
