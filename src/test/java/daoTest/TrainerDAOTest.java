package daoTest;

import dao.TrainerDAO;
import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.TrainerStorage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOTest {

    private TrainerDAO trainerDAO;
    private Map<Long, Trainer> storageMap;

    @BeforeEach
    void setUp() {
        storageMap = new HashMap<>();
        TrainerStorage mockStorage = new TrainerStorage(null) {
            @Override
            public Map<Long, Trainer> getStorage() {
                return storageMap;
            }
        };
        trainerDAO = new TrainerDAO(mockStorage);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainer.setFirstName("Donyor");
        trainer.setLastName("Alibekov");
        trainer.setUsername("Donyor.Alibekov");

        trainerDAO.createTrainer(trainer);

        assertTrue(storageMap.containsKey(1L));
        assertEquals("Donyor.Alibekov", storageMap.get(1L).getUsername());
    }

    @Test
    void testCreateTrainer_duplicateUsername() {
        Trainer t1 = new Trainer();
        t1.setUserId(1L);
        t1.setFirstName("Donyor");
        t1.setLastName("Alibekov");
        t1.setUsername("donyor.alibekov");
        storageMap.put(1L, t1);

        Trainer t2 = new Trainer();
        t2.setUserId(2L);
        t2.setFirstName("Donyor");
        t2.setLastName("Alibekov");
        t2.setUsername("donyor.alibekov");

        trainerDAO.createTrainer(t2);

        assertEquals("1donyor.alibekov", storageMap.get(2L).getUsername());
    }

    @Test
    void testUpdateTrainer() throws NoSuchTrainerException {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainer.setFirstName("Donyor");
        trainer.setLastName("Alibekov");
        trainer.setUsername("donyor.alibekov");
        storageMap.put(1L, trainer);

        trainer.setUsername("donyor.updated");
        trainerDAO.updateTrainer(trainer);

        assertEquals("donyor.updated", storageMap.get(1L).getUsername());
    }

    @Test
    void testUpdateTrainer_notFound() {
        Trainer trainer = new Trainer();
        trainer.setUserId(99L);

        assertThrows(NoSuchTrainerException.class, () -> trainerDAO.updateTrainer(trainer));
    }

    @Test
    void testSelectTrainer() throws NoSuchTrainerException {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        storageMap.put(1L, trainer);

        Trainer selected = trainerDAO.selectTrainer(1L);
        assertSame(trainer, selected);
    }

    @Test
    void testSelectTrainer_notFound() {
        assertThrows(NoSuchTrainerException.class, () -> trainerDAO.selectTrainer(99L));
    }
}