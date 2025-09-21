package storage;

import entity.Trainer;
import entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.PasswordGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerStorageTest {

    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        passwordGenerator = new PasswordGenerator();
    }

    @Test
    void testInitAndGetStorage() throws Exception {
        File tempFile = File.createTempFile("trainer_test", ".txt");
        Map<Long, Trainer> map = getTrainerMap(tempFile);
        assertEquals(2, map.size());

        Trainer john = map.get(1L);
        assertNotNull(john);
        assertEquals("John", john.getFirstName());
        assertEquals("Doe", john.getLastName());
        List<TrainingType> johnSpecializations = john.getSpecialization();
        assertTrue(johnSpecializations.contains(TrainingType.CARDIO));
        assertTrue(johnSpecializations.contains(TrainingType.RUNNING));

        Trainer jane = map.get(2L);
        assertNotNull(jane);
        assertEquals("Jane", jane.getFirstName());
        assertEquals("Smith", jane.getLastName());
        List<TrainingType> janeSpecializations = jane.getSpecialization();
        assertTrue(janeSpecializations.contains(TrainingType.WEIGHTLIFTING));
        assertTrue(janeSpecializations.contains(TrainingType.PUSH_UP));
    }

    private Map<Long, Trainer> getTrainerMap(File tempFile) throws IOException, NoSuchFieldException, IllegalAccessException {
        tempFile.deleteOnExit();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,John,Doe,cardio,running\n");
            writer.write("2,Jane,Smith,weightlifting,push_up\n");
        }

        TrainerStorage storage = new TrainerStorage(passwordGenerator);
        java.lang.reflect.Field filePathField = TrainerStorage.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(storage, tempFile.getAbsolutePath());

        storage.init();

        return storage.getStorage();
    }
}