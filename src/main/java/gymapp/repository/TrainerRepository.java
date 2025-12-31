package gymapp.repository;

import gymapp.entity.Trainer;
import gymapp.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findByUsernameContaining(String username);

    @Query("SELECT t FROM Training t WHERE t.trainer.username = :username")
    List<Training> findTrainingsByTrainerUsername(@Param("username") String username);

    @Query("SELECT t FROM Training t WHERE t.trainee.firstName = :firstName")
    List<Training> findTrainingsByTraineeFirstName(@Param("firstName") String firstName);
}