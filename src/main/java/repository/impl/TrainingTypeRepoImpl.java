package repository.impl;

import entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TrainingTypeRepo;

@AllArgsConstructor
@Repository
public class TrainingTypeRepoImpl implements TrainingTypeRepo {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void createTrainingType(TrainingType trainingType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(trainingType);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }

    }
}
