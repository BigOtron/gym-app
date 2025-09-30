package repository.impl;

import entity.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TrainerRepo;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class TrainerRepoImpl implements TrainerRepo {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void createTrainer(Trainer trainer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(trainer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(trainer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Trainer> selectTrainer(String username) {
        String query = "SELECT t FROM Trainer t WHERE t.username = :username";
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.of(entityManager.createQuery(query, Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
