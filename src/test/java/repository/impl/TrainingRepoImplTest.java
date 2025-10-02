package repository.impl;

import entity.Trainee;
import entity.Trainer;
import entity.Training;
import entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import repository.TrainingRepo;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingRepoImplTest {

    private static EntityManagerFactory emf;
    private static TrainingRepo trainingRepo;
    private TrainingType trainingType;
    private Trainer trainer;
    private Trainee trainee;

    @BeforeAll
    static void setupEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("gym-app");
        trainingRepo = new TrainingRepoImpl(emf);
    }

    @BeforeEach
    void setupEntities() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        trainingType = new TrainingType();
        trainingType.setSpecialization("Cardio");
        em.persist(trainingType);

        trainer = new Trainer("ahmad", "ahmadov", trainingType);
        trainer.setUsername("ahmad.ahmadov");
        trainer.setPasswordHash("1234567890");
        em.persist(trainer);


        trainee = new Trainee("ahmed", "ahmedov",
                new Date(System.currentTimeMillis() - 1000000000L), "Some address");
        trainee.setPasswordHash("1234567890");
        trainee.setUsername("ahmed.ahmedov");
        em.persist(trainee);

        em.getTransaction().commit();
        em.close();
    }

    @AfterEach
    void cleanup() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Training").executeUpdate();
        em.createQuery("DELETE FROM Trainee").executeUpdate();
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

    private Training buildTraining(String name) {
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName(name);
        training.setTrainingType(trainingType);
        training.setTrainingDate(new Date(System.currentTimeMillis() + 1000*60*60*24));
        training.setDuration(60);
        return training;
    }

    @Test
    void testCreateAndSelectTraining() {
        Training training = buildTraining("Morning Cardio");
        trainingRepo.createTraining(training);

        Optional<Training> found = trainingRepo.selectTraining(training.getId());
        assertTrue(found.isPresent());
        assertEquals("Morning Cardio", found.get().getTrainingName());
        assertEquals(trainer.getUsername(), found.get().getTrainer().getUsername());
    }

    @Test
    void testSelectTraining_NotFound() {
        Optional<Training> found = trainingRepo.selectTraining(UUID.randomUUID());
        assertTrue(found.isEmpty());
    }
}