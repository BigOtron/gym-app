package serviceTest;

import dao.TrainingDAO;
import entity.Training;
import exceptions.NoSuchTrainingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TrainingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingDAO trainingDAO;
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingDAO = mock(TrainingDAO.class);
        trainingService = new TrainingService(trainingDAO);
    }

    @Test
    void testCreateTraining() {
        Training training = new Training();
        training.setTrainingId(1L);

        trainingService.createTraining(training);

        verify(trainingDAO, times(1)).createTraining(training);
    }

    @Test
    void testSelectTraining() throws NoSuchTrainingException {
        Training training = new Training();
        training.setTrainingId(2L);
        when(trainingDAO.selectTraining(2L)).thenReturn(training);

        Training result = trainingService.selectTraining(2L);

        assertSame(training, result);
        verify(trainingDAO, times(1)).selectTraining(2L);
    }

    @Test
    void testSelectTraining_notFound() throws NoSuchTrainingException {
        when(trainingDAO.selectTraining(99L)).thenThrow(new NoSuchTrainingException());

        assertThrows(RuntimeException.class, () -> trainingService.selectTraining(99L));
        verify(trainingDAO, times(1)).selectTraining(99L);
    }
}
