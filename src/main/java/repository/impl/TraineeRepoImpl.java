package repository.impl;

import entity.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TraineeRepo;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class TraineeRepoImpl implements TraineeRepo {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void createTrainee(Trainee trainee) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(trainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Trainee> selectTrainee(String username) {
        String query = "SELECT t FROM Trainee t WHERE t.username = :username";
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.of(entityManager.createQuery(query, Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteTrainee(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            String query = "SELECT t FROM Trainee t WHERE t.username = :username";
            entityManager.createQuery(query, Trainee.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst().ifPresent(entityManager::remove);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
