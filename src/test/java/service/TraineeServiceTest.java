package service;

import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.TraineeRepo;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    private TraineeRepo traineeRepo;
    private TraineeService traineeService;

    @BeforeEach
    void setup() {
        traineeRepo = mock(TraineeRepo.class);
        traineeService = new TraineeService(traineeRepo);
    }

    private Trainee buildTrainee(String username) {
        Trainee trainee = new Trainee("John", "Doe",
                new Date(System.currentTimeMillis() - 1000000000L), "Some Address");
        trainee.setUsername(username);
        trainee.setPasswordHash("1234567890");
        return trainee;

    }

    @Test
    void testNewUsername() {
        Trainee trainee = buildTrainee("jdoe");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.empty());

        traineeService.createTrainee(trainee);


        assertEquals("John.Doe", trainee.getUsername());
        verify(traineeRepo).createTrainee(trainee);
    }

    @Test
    void testAlreadyUsernameBehaviour() {
        Trainee trainee = buildTrainee("jdoe");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));
        when(traineeRepo.selectByUsernameContaining("jdoe")).thenReturn(
                java.util.List.of(buildTrainee("jdoe"), buildTrainee("jdoe2"))
        );

        traineeService.createTrainee(trainee);

        verify(traineeRepo).createTrainee(trainee);
    }

    @Test
    void testSelectTraineeFound() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        Trainee found = traineeService.selectTrainee("jdoe");
        assertEquals("jdoe", found.getUsername());
    }

    @Test
    void testSelectTraineeNotFound() {
        when(traineeRepo.selectTrainee("ghost")).thenReturn(Optional.empty());

        assertThrows(NoSuchTraineeException.class,
                () -> traineeService.selectTrainee("ghost"));
    }

    @Test
    void testActivateTrainee() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setIsActive(false);
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        traineeService.activateTrainee("jdoe");

        assertTrue(trainee.getIsActive());
        verify(traineeRepo).updateTrainee(trainee);
    }

    @Test
    void testDeactivateTrainee() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setIsActive(true);
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        traineeService.deactivateTrainee("jdoe");

        assertFalse(trainee.getIsActive());
        verify(traineeRepo).updateTrainee(trainee);
    }

    @Test
    void testDeleteTrainee() {
        traineeService.deleteTrainee("jdoe");
        verify(traineeRepo).deleteTrainee("jdoe");
    }

    @Test
    void testValidateCredentialsSuccess() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setPasswordHash("secret");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        assertTrue(traineeService.validateTraineeCredentials("jdoe", "secret"));
    }

    @Test
    void testValidateCredentialsFailure() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setPasswordHash("secret");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        assertFalse(traineeService.validateTraineeCredentials("jdoe", "wrongpass"));
    }

    @Test
    void testChangePasswordSuccess() throws NoSuchTraineeException {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setPasswordHash("oldpass");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        traineeService.changePassword("jdoe", "oldpass", "newpass");

        assertEquals("newpass", trainee.getPasswordHash());
        verify(traineeRepo).updateTrainee(trainee);
    }

    @Test
    void testChangePasswordWrongOldPassword() {
        Trainee trainee = buildTrainee("jdoe");
        trainee.setPasswordHash("oldpass");
        when(traineeRepo.selectTrainee("jdoe")).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.changePassword("jdoe", "wrong", "newpass"));
    }
}