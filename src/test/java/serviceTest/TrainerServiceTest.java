package serviceTest;


import dao.TrainerDAO;
import entity.Trainer;
import exceptions.NoSuchTrainerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TrainerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDAO trainerDAO;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerDAO = mock(TrainerDAO.class);
        trainerService = new TrainerService(trainerDAO);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        trainerService.createTrainer(trainer);

        verify(trainerDAO, times(1)).createTrainer(trainer);
    }

    @Test
    void testSelectTrainer() throws NoSuchTrainerException {
        Trainer trainer = new Trainer();
        trainer.setUserId(2L);
        when(trainerDAO.selectTrainer(2L)).thenReturn(trainer);

        Trainer result = trainerService.selectTrainer(2L);

        assertSame(trainer, result);
        verify(trainerDAO, times(1)).selectTrainer(2L);
    }

    @Test
    void testSelectTrainer_notFound() throws NoSuchTrainerException {
        when(trainerDAO.selectTrainer(99L)).thenThrow(new NoSuchTrainerException());

        assertThrows(RuntimeException.class, () -> trainerService.selectTrainer(99L));
        verify(trainerDAO, times(1)).selectTrainer(99L);
    }

    @Test
    void testUpdateTrainer() throws NoSuchTrainerException {
        Trainer trainer = new Trainer();
        trainer.setUserId(3L);

        doNothing().when(trainerDAO).updateTrainer(trainer);

        trainerService.updateTrainer(trainer);

        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }

    @Test
    void testUpdateTrainer_notFound() throws NoSuchTrainerException {
        Trainer trainer = new Trainer();
        trainer.setUserId(99L);

        doThrow(new NoSuchTrainerException()).when(trainerDAO).updateTrainer(trainer);

        assertThrows(RuntimeException.class, () -> trainerService.updateTrainer(trainer));
        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }
}
