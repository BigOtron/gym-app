package storage;

import entity.Address;
import entity.Trainee;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utility.PasswordGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring component responsible for storing {@link Trainee} entities.
 * <p>
 * Loads trainee data from an external CSV file specified by the property
 * {@code storage.file.path.trainee} during initialization. Each trainee
 * is assigned a generated password using {@link PasswordGenerator}.
 * <p>
 * Provides access to the in-memory storage map via {@link #getStorage()}.
 * Each line in the CSV file should represent a training record with comma-separated values:
 *   <ul>
 *        <li>Trainee ID</li>
 *        <li>Trainee first name</li>
 *        <li>Trainee last name</li>
 *        <li>Trainee date of birth</li>
 *        <li>Trainee address</li>
 *    </ul>
 */
@Component
public class TraineeStorage {
    private final Map<Long, Trainee> storage = new HashMap<>();
    private final PasswordGenerator passwordGenerator;
    private static final Logger logger = LoggerFactory.getLogger(TraineeStorage.class);

    @Value("${storage.file.path.trainee}")
    private String filePath;

    public TraineeStorage(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    public Map<Long, Trainee> getStorage() {
        return storage;
    }

    @PostConstruct
    public void init() throws IOException {
        logger.info("Initializing TraineeStorage from file: {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Reading file data: {}", line);
                String[] lineData = line.split(",");
                Trainee trainee = parseTrainee(lineData);
                storage.put(trainee.getUserId(), trainee);
            }
        }
    }

    private Trainee parseTrainee(String[] data) {
        Trainee trainee = new Trainee();
        trainee.setUserId(Long.parseLong(data[0]));
        trainee.setFirstName(data[1]);
        trainee.setLastName(data[2]);
        trainee.setUsername(data[1] + "." + data[2]);
        trainee.setPassword(passwordGenerator.generatePassword(10));
        trainee.setDateOfBirth(parseDateOfBirth(data[3]));
        trainee.setAddress(parseAddress(data));
        return trainee;
    }

    private LocalDate parseDateOfBirth(String datum) {
        String[] datumArr = datum.split("-");
        int year = Integer.parseInt(datumArr[0]);
        int month = Integer.parseInt(datumArr[1]);
        int day = Integer.parseInt(datumArr[2]);

        return LocalDate.of(year, month, day);
    }

    private Address parseAddress(String[] data) {
        Address address = new Address();
        address.setStreet(data[4]);
        address.setCity(data[5]);
        address.setPostalCode(data[6]);
        address.setCountry(data[7]);

        return  address;
    }


}
