package gymapp.repository.impl;


import gymapp.entity.Trainee;

import gymapp.entity.Trainer;
import gymapp.entity.Training;
import gymapp.entity.TrainingType;
import jakarta.persistence.EntityManager;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeRepoImplTest {

    private static EntityManagerFactory emf;
    private static TraineeRepoImpl traineeRepo;
    private static TrainingRepoImpl trainingRepo;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("gym-app");
        traineeRepo = new TraineeRepoImpl(emf);
        trainingRepo = new TrainingRepoImpl(emf);
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

    private Training buildTraining(String name, Trainee trainee) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TrainingType trainingType = new TrainingType();
        trainingType.setSpecialization("pull-up");
        em.persist(trainingType);

        Trainer trainer = new Trainer("ahmad", "ahmadov", trainingType);
        trainer.setUsername("ahmad.ahmadov");
        trainer.setPasswordHash("1234567890");
        em.persist(trainer);

        em.getTransaction().commit();
        em.close();

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

    @Test
    void testSelectTrainingsByUsername() {
        Trainee trainee = buildTrainee("azim.azimov");
        traineeRepo.createTrainee(trainee);
        Training training1 = buildTraining("training1", trainee);
        trainingRepo.createTraining(training1);

        List<Training> trainings = traineeRepo.selectTrainingsByUsername(trainee.getUsername());

        assertFalse(trainings.isEmpty());
        assertEquals(1, trainings.size());
        assertEquals(trainings.get(0).getTrainee().getId(), trainee.getId());

    }

    @Test
    void testSelectTrainingsByTraineeFirstName() {
        Trainee trainee = buildTrainee("farrux.farruxov");

        traineeRepo.createTrainee(trainee);
        Training training1 = buildTraining("training1", trainee);
        trainingRepo.createTraining(training1);

        List<Training> trainings = traineeRepo.selectTrainingsByTrainerFirstName(training1.getTrainer().getFirstName());
        assertFalse(trainings.isEmpty());
        assertEquals(1, trainings.size());
        assertEquals(trainings.get(0).getTrainee().getId(), trainee.getId());
    }
}