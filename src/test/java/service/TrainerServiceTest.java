package service;

import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.TrainerRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    @Mock
    private TrainerRepo trainerRepo;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("John.Doe");
        trainer.setPasswordHash("oldPass");
        trainer.setIsActive(Boolean.FALSE);
    }

    @Test
    void testCreateTrainerNewUsername() {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.empty());

        trainerService.createTrainer(trainer);

        assertTrue(trainer.getUsername().startsWith("John.Doe"));
        assertNotNull(trainer.getPasswordHash());
        verify(trainerRepo).createTrainer(trainer);
    }

    @Test
    void testCreateTrainerUsernameAlreadyExists() {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));
        when(trainerRepo.selectByUsernameContaining("John.Doe"))
                .thenReturn(List.of(trainer, trainer));

        trainerService.createTrainer(trainer);

        verify(trainerRepo).createTrainer(trainer);
    }

    @Test
    void testUpdateTrainer() {
        trainerService.updateTrainer(trainer);
        verify(trainerRepo).updateTrainer(trainer);
    }

    @Test
    void testSelectTrainerFound() throws NoSuchTrainerException {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        Trainer found = trainerService.selectTrainer("John.Doe");

        assertEquals("John.Doe", found.getUsername());
    }

    @Test
    void testSelectTrainerNotFound() {
        when(trainerRepo.selectTrainer("Unknown")).thenReturn(Optional.empty());

        assertThrows(NoSuchTrainerException.class,
                () -> trainerService.selectTrainer("Unknown"));
    }

    @Test
    void testActivateTrainer() throws NoSuchTrainerException {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        trainerService.activateTrainer("John.Doe");

        assertTrue(trainer.getIsActive());
        verify(trainerRepo).updateTrainer(trainer);
    }

    @Test
    void testDeactivateTrainer() throws NoSuchTrainerException {
        trainer.setIsActive(Boolean.TRUE);
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        trainerService.deactivateTrainer("John.Doe");

        assertFalse(trainer.getIsActive());
        verify(trainerRepo).updateTrainer(trainer);
    }

    @Test
    void testValidateTrainerCredentialsValid() throws NoSuchTrainerException {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        boolean result = trainerService.validateTrainerCredentials("John.Doe", "oldPass");

        assertTrue(result);
    }

    @Test
    void testValidateTrainerCredentialsInvalid() throws NoSuchTrainerException {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        boolean result = trainerService.validateTrainerCredentials("John.Doe", "wrongPass");

        assertFalse(result);
    }

    @Test
    void testChangePasswordSuccess() throws NoSuchTrainerException {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        trainerService.changePassword("John.Doe", "oldPass", "newPass");

        assertEquals("newPass", trainer.getPasswordHash());
        verify(trainerRepo).updateTrainer(trainer);
    }

    @Test
    void testChangePasswordWrongOldPassword() {
        when(trainerRepo.selectTrainer("John.Doe")).thenReturn(Optional.of(trainer));

        assertThrows(IllegalArgumentException.class,
                () -> trainerService.changePassword("John.Doe", "wrongPass", "newPass"));
    }
}