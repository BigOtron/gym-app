package storage;

import entity.Training;
import entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingStorageTest {

    private TrainingStorage storage;

    @BeforeEach
    void setUp() throws Exception {
        storage = new TrainingStorage();

        File tempFile = File.createTempFile("training_test", ".txt");
        tempFile.deleteOnExit();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,101,201,Yoga,CARDIO,2025-09-16 10:30,60\n");
            writer.write("2,102,202,Pilates,RUNNING,2025-09-17 14:00,45\n");
        }

        java.lang.reflect.Field filePathField = TrainingStorage.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(storage, tempFile.getAbsolutePath());

        storage.init();
    }

    @Test
    void testStorageInitialization() {
        Map<Long, Training> map = storage.getStorage();
        assertEquals(2, map.size());

        Training yoga = map.get(1L);
        assertNotNull(yoga);
        assertEquals(101L, yoga.getTraineeId());
        assertEquals(201L, yoga.getTrainerId());
        assertEquals("Yoga", yoga.getTrainingName());
        assertEquals(TrainingType.CARDIO, yoga.getTrainingType());
        assertEquals(Duration.ofMinutes(60), yoga.getDuration());
        assertEquals(LocalDateTime.of(2025, 9, 16, 10, 30), yoga.getTrainingTime());

        Training pilates = map.get(2L);
        assertNotNull(pilates);
        assertEquals(102L, pilates.getTraineeId());
        assertEquals(202L, pilates.getTrainerId());
        assertEquals("Pilates", pilates.getTrainingName());
        assertEquals(TrainingType.RUNNING, pilates.getTrainingType());
        assertEquals(Duration.ofMinutes(45), pilates.getDuration());
        assertEquals(LocalDateTime.of(2025, 9, 17, 14, 0), pilates.getTrainingTime());
    }
}
