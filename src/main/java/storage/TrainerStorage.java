package storage;

import entity.Trainer;
import entity.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utility.PasswordGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class TrainerStorage {
    private final Map<Long, Trainer> storage = new HashMap<>();
    private final PasswordGenerator passwordGenerator;

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
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
