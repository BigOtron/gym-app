package repository.impl;

import entity.Trainee;
import entity.Trainer;
import entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repository.TraineeRepo;

import java.util.Collections;
import java.util.List;
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
    public List<Trainee> selectByUsernameContaining(String username) {
        String query = "SELECT t FROM Trainee t WHERE t.username LIKE :username";
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query, Trainee.class)
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
                    "SELECT t from Training t WHERE t.trainee.username = :username", Training.class
            ).setParameter("username",username)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Training> selectTrainingsByTrainerFirstName(String firstName) {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                            "SELECT t from Training t WHERE t.trainer.firstName = :firstName", Training.class
                    ).setParameter("firstName",firstName)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Trainer> selectTrainersNotAssignedByUsername(String username) {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(
                    "SELECT tr FROM Trainer tr " +
                            "WHERE tr.id NOT IN (" +
                            "   SELECT t.trainer.id FROM Training t WHERE t.trainee.username = :username" +
                            ")", Trainer.class
            ).setParameter("username", username)
                    .getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
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
