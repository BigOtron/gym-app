package storageTest;

import entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.TraineeStorage;
import utility.PasswordGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraineeStorageTest {

    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        passwordGenerator = new PasswordGenerator();
    }

    @Test
    void testInitAndGetStorage() throws Exception {
        File tempFile = File.createTempFile("trainee_test", ".txt");
        Map<Long, Trainee> map = getTraineeMap(tempFile);
        assertEquals(2, map.size());

        Trainee john = map.get(1L);
        assertNotNull(john);
        assertEquals("John", john.getFirstName());
        assertEquals("Doe", john.getLastName());
        assertEquals(LocalDate.of(1990, 1, 15), john.getDateOfBirth());

        Trainee jane = map.get(2L);
        assertNotNull(jane);
        assertEquals("Jane", jane.getFirstName());
        assertEquals("Smith", jane.getLastName());
    }

    private Map<Long, Trainee> getTraineeMap(File tempFile) throws IOException, NoSuchFieldException, IllegalAccessException {
        tempFile.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,John,Doe,1990-01-15,Street A,City X,12345,Country Y\n");
            writer.write("2,Jane,Smith,1992-06-30,Street B,City Z,67890,Country Y\n");
        }

        TraineeStorage storage = new TraineeStorage(passwordGenerator);
        java.lang.reflect.Field filePathField = TraineeStorage.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(storage, tempFile.getAbsolutePath());

        storage.init();

        return storage.getStorage();
    }
}
