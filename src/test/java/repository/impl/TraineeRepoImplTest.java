package repository.impl;


import entity.Trainee;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeRepoImplTest {

    private static EntityManagerFactory emf;
    private static TraineeRepoImpl traineeRepo;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("gym-app");
        traineeRepo = new TraineeRepoImpl(emf);
    }

    @AfterEach
    void cleanup() {
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Training").executeUpdate();
        em.createQuery("DELETE FROM Trainer").executeUpdate();
        em.createQuery("DELETE FROM TrainingType").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    private Trainee buildTrainee(String username) {
        Trainee trainee = new Trainee(
                "azim", "azimov",
                new Date(System.currentTimeMillis() - 1000000),
                "Test Address"
        );
        trainee.setUsername(username);
        trainee.setPasswordHash("1234567890");
        return trainee;
    }

    @Test
    void testCreateAndSelectTrainee() {
        Trainee trainee = buildTrainee("trainee1");
        traineeRepo.createTrainee(trainee);

        Optional<Trainee> found = traineeRepo.selectTrainee("trainee1");
        assertTrue(found.isPresent());
        assertEquals("trainee1", found.get().getUsername());
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = buildTrainee("trainee2");
        traineeRepo.createTrainee(trainee);

        trainee.setAddress("New Address");
        traineeRepo.updateTrainee(trainee);

        Optional<Trainee> found = traineeRepo.selectTrainee("trainee2");
        assertTrue(found.isPresent());
        assertEquals("New Address", found.get().getAddress());
    }

    @Test
    void testSelectByUsernameContaining() {
        traineeRepo.createTrainee(buildTrainee("azam.azamov"));
        traineeRepo.createTrainee(buildTrainee("1azam.azamov"));
        traineeRepo.createTrainee(buildTrainee("2azam.azamov"));
        traineeRepo.createTrainee(buildTrainee("3azam.azamov"));
        traineeRepo.createTrainee(buildTrainee("rustam.rustamov"));

        List<Trainee> results = traineeRepo.selectByUsernameContaining("azam.azamov");
        assertEquals(4, results.size());
    }

    @Test
    void testDeleteTrainee() {
        traineeRepo.createTrainee(buildTrainee("todelete"));
        traineeRepo.deleteTrainee("todelete");

        Optional<Trainee> found = traineeRepo.selectTrainee("todelete");
        assertTrue(found.isEmpty());
    }
}