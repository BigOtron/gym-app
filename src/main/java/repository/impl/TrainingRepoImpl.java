package repository.impl;

import entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TrainingRepo;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Repository
public class TrainingRepoImpl implements TrainingRepo {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void createTraining(Training training) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(training);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Training> selectTraining(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Training training = entityManager.find(Training.class, id);
        return training == null ? Optional.empty() : Optional.of(training);
    }
}
