package dao;

import entity.Training;
import exceptions.NoSuchTrainingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.TrainingStorage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {

    private TrainingDAO trainingDAO;
    private Map<Long, Training> storageMap;

    @BeforeEach
    void setUp() {
        storageMap = new HashMap<>();
        TrainingStorage mockStorage = new TrainingStorage() {
            @Override
            public Map<Long, Training> getStorage() {
                return storageMap;
            }
        };
        trainingDAO = new TrainingDAO(mockStorage);
    }

    @Test
    void testCreateTraining() {
        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainingName("Cardio Basics");

        trainingDAO.createTraining(training);

        assertTrue(storageMap.containsKey(1L));
        assertEquals("Cardio Basics", storageMap.get(1L).getTrainingName());
    }

    @Test
    void testSelectTraining() throws NoSuchTrainingException {
        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainingName("Strength Training");
        storageMap.put(1L, training);

        Training selected = trainingDAO.selectTraining(1L);
        assertSame(training, selected);
    }

    @Test
    void testSelectTraining_notFound() {
        assertThrows(NoSuchTrainingException.class, () -> trainingDAO.selectTraining(99L));
    }
}
