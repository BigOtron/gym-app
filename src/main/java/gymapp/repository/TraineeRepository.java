
package gymapp.repository;

import gymapp.entity.Trainee;
import gymapp.entity.Trainer;
import gymapp.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUsername(String username);

    List<Trainee> findByUsernameContaining(String username);

    @Query("SELECT t FROM Training t WHERE t.trainee.username = :username")
    List<Training> findTrainingsByTraineeUsername(@Param("username") String username);

    @Query("SELECT t FROM Training t WHERE t.trainer.firstName = :firstName")
    List<Training> findTrainingsByTrainerFirstName(@Param("firstName") String firstName);

    @Query("""
        SELECT tr FROM Trainer tr
        WHERE tr NOT IN (
            SELECT t.trainer FROM Trainee te JOIN te.trainings t
            WHERE te.username = :username
        )
    """)
    List<Trainer> findTrainersNotAssignedToTrainee(@Param("username") String username);
}