package service;

import dao.TraineeDAO;
import entity.Trainee;
import exceptions.NoSuchTraineeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    private TraineeDAO traineeDAO;
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeDAO = mock(TraineeDAO.class);
        traineeService = new TraineeService(traineeDAO);
    }

    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);

        traineeService.createTrainee(trainee);

        verify(traineeDAO, times(1)).createTrainee(trainee);
    }

    @Test
    void testSelectTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(2L);
        when(traineeDAO.selectTrainee(2L)).thenReturn(trainee);

        Trainee result = traineeService.selectTrainee(2L);

        assertSame(trainee, result);
        verify(traineeDAO, times(1)).selectTrainee(2L);
    }

    @Test
    void testSelectTrainee_notFound() throws NoSuchTraineeException {
        when(traineeDAO.selectTrainee(99L)).thenThrow(new NoSuchTraineeException());

        assertThrows(RuntimeException.class, () -> traineeService.selectTrainee(99L));
        verify(traineeDAO, times(1)).selectTrainee(99L);
    }

    @Test
    void testUpdateTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);

        doNothing().when(traineeDAO).updateTrainee(trainee);

        traineeService.updateTrainee(trainee);

        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }

    @Test
    void testUpdateTrainee_notFound() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(99L);

        doThrow(new NoSuchTraineeException()).when(traineeDAO).updateTrainee(trainee);

        assertThrows(RuntimeException.class, () -> traineeService.updateTrainee(trainee));
        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }

    @Test
    void testDeleteTrainee() throws NoSuchTraineeException {
        Trainee trainee = new Trainee();
        trainee.setUserId(4L);

        when(traineeDAO.deleteTrainee(4L)).thenReturn(trainee);

        Trainee deleted = traineeService.deleteTrainee(4L);

        assertSame(trainee, deleted);
        verify(traineeDAO, times(1)).deleteTrainee(4L);
    }

    @Test
    void testDeleteTrainee_notFound() throws NoSuchTraineeException {
        doThrow(new NoSuchTraineeException()).when(traineeDAO).deleteTrainee(99L);

        assertThrows(RuntimeException.class, () -> traineeService.deleteTrainee(99L));
        verify(traineeDAO, times(1)).deleteTrainee(99L);
    }
}
