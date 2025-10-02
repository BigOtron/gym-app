package repository.impl;

import entity.Trainee;
import entity.Trainer;
import entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TrainerRepo;

import java.util.Collections;
import java.util.List;
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

    @Override
    public List<Trainer> selectByUsernameContaining(String username) {
        String query = "SELECT t FROM Trainer t WHERE t.username LIKE :username";
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query, Trainer.class)
                    .setParameter("username", "%" + username + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Training> selectTrainingsByUsername(String username) {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT t from Training t WHERE t.trainer.username = :username", Training.class
                    ).setParameter("username",username)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Training> selectTrainingsByTraineeFirstName(String firstName) {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT t from Training t WHERE t.trainee.firstName = :firstName", Training.class
                    ).setParameter("firstName",firstName)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
