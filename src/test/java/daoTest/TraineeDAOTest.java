package daoTest;

import dao.TraineeDAO;
import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.TraineeStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO traineeDAO;
    private Map<Long, Trainee> storageMap;

    @BeforeEach
    void setUp() {
        storageMap = new HashMap<>();
        TraineeStorage mockStorage = new TraineeStorage(null) {
            @Override
            public Map<Long, Trainee> getStorage() {
                return storageMap;
            }
        };
        traineeDAO = new TraineeDAO(mockStorage);
    }

    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("Donyor");
        trainee.setLastName("Alibekov");
        trainee.setUsername("donyor.alibekov");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));

        traineeDAO.createTrainee(trainee);

        assertTrue(storageMap.containsKey(1L));
        assertEquals("donyor.alibekov", storageMap.get(1L).getUsername());
    }

    @Test
    void testCreateTrainee_duplicateUsername() {
        Trainee t1 = new Trainee();
        t1.setUserId(1L);
        t1.setFirstName("Donyor");
        t1.setLastName("Alibekov");
        t1.setUsername("donyor.alibekov");
        storageMap.put(1L, t1);

        Trainee t2 = new Trainee();
        t2.setUserId(2L);
        t2.setFirstName("Donyor");
        t2.setLastName("Alibekov");
        t2.setUsername("donyor.alibekov");

        traineeDAO.createTrainee(t2);

        assertEquals("1donyor.alibekov", storageMap.get(2L).getUsername());
    }

    @Test
    void testUpdateTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("Donyor");
        trainee.setLastName("Alibekov");
        trainee.setUsername("donyor.alibekov");

        storageMap.put(1L, trainee);

        trainee.setUsername("donyor.alibekov.updated");
        traineeDAO.updateTrainee(trainee);

        assertEquals("donyor.alibekov.updated", storageMap.get(1L).getUsername());
    }

    @Test
    void testUpdateTrainee_notFound() {
        Trainee trainee = new Trainee();
        trainee.setUserId(99L);

        assertThrows(NoSuchTraineeException.class, () -> traineeDAO.updateTrainee(trainee));
    }

    @Test
    void testSelectTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        storageMap.put(1L, trainee);

        Trainee selected = traineeDAO.selectTrainee(1L);
        assertSame(trainee, selected);
    }

    @Test
    void testSelectTrainee_notFound() {
        assertThrows(NoSuchTraineeException.class, () -> traineeDAO.selectTrainee(99L));
    }

    @Test
    void testDeleteTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        storageMap.put(1L, trainee);

        Trainee deleted = traineeDAO.deleteTrainee(1L);
        assertSame(trainee, deleted);
        assertFalse(storageMap.containsKey(1L));
    }

    @Test
    void testDeleteTrainee_notFound() {
        assertThrows(NoSuchTraineeException.class, () -> traineeDAO.deleteTrainee(99L));
    }
}