package repository.impl;

import entity.Trainee;
import entity.Trainer;
import entity.Training;
import entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.TrainerRepo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerRepoImplTest {

    private static EntityManagerFactory emf;
    private static TrainerRepo trainerRepo;
    private TrainingType trainingType;

    @BeforeAll
    static void setupEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("gym-app");
        trainerRepo = new TrainerRepoImpl(emf);
    }

    @BeforeEach
    void setupTrainingType() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        trainingType = new TrainingType();
        trainingType.setSpecialization("Strength");
        entityManager.persist(trainingType);
        entityManager.getTransaction().commit();
        entityManager.close();
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

    private Trainer buildTrainer(String username) {
        Trainer trainer = new Trainer("rustam", "rustamov",trainingType);
        trainer.setUsername(username);
        trainer.setPasswordHash("1234567890");
        return trainer;
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = buildTrainer("trainer1");
        trainerRepo.createTrainer(trainer);

        Optional<Trainer> found = trainerRepo.selectTrainer("trainer1");
        assertTrue(found.isPresent());
        assertEquals("trainer1", found.get().getUsername());
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = buildTrainer("trainer2");
        trainerRepo.createTrainer(trainer);

        trainer.setLastName("Azam");
        trainerRepo.updateTrainer(trainer);

        Optional<Trainer> updated = trainerRepo.selectTrainer("trainer2");
        assertTrue(updated.isPresent());
        assertEquals("Azam", updated.get().getLastName());
    }

    @Test
    void testSelectTrainer_NotFound() {
        Optional<Trainer> trainer = trainerRepo.selectTrainer("ghost");
        assertTrue(trainer.isEmpty());
    }

    @Test
    void testSelectByUsernameContaining() {
        trainerRepo.createTrainer(buildTrainer("alpha"));
        trainerRepo.createTrainer(buildTrainer("beta"));

        List<Trainer> results = trainerRepo.selectByUsernameContaining("a");
        assertEquals(2, results.size());
    }

    @Test
    void testSelectTrainingsByUsername() {
        Trainer trainer = buildTrainer("trainer3");
        trainerRepo.createTrainer(trainer);

        Trainee trainee = new Trainee("Rustam", "rustamov",new Date(), "Address");
        trainee.setUsername("rustam.rustamov");
        trainee.setPasswordHash("1234567890");
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("Leg Day");
        training.setTrainingType(trainingType);
        training.setTrainingDate(new Date(System.currentTimeMillis() + 1000*60*60*24));
        training.setDuration(45);

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.persist(training);
        entityManager.getTransaction().commit();
        entityManager.close();

        List<Training> trainings = trainerRepo.selectTrainingsByUsername("trainer3");
        assertEquals(1, trainings.size());
        assertEquals("Leg Day", trainings.get(0).getTrainingName());
    }

    @Test
    void testSelectTrainingsByTraineeFirstName() {
        Trainer trainer = buildTrainer("trainer4");
        trainerRepo.createTrainer(trainer);

        Trainee trainee = new Trainee("abbos", "abbosov",  new Date(), "Address");
        trainee.setUsername("abbos.abbosov");
        trainee.setPasswordHash("1234567890");
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("Push Day");
        training.setTrainingType(trainingType);
        training.setTrainingDate(new Date(System.currentTimeMillis() + 1000*60*60*24));
        training.setDuration(40);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(trainee);
        em.persist(training);
        em.getTransaction().commit();
        em.close();

        List<Training> trainings = trainerRepo.selectTrainingsByTraineeFirstName("abbos");
        assertEquals(1, trainings.size());
        assertEquals("Push Day", trainings.get(0).getTrainingName());
    }
}