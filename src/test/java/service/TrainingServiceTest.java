package service;

import entity.Training;
import exceptions.NoSuchTrainingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.TrainingRepo;


import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private TrainingRepo trainingRepo;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        training = new Training();
        training.setId(UUID.randomUUID());
        training.setTrainingName("Strength Training");
        training.setTrainingDate(new Date(System.currentTimeMillis() + 1000*60*60));
    }

    @Test
    void testCreateTraining() {
        trainingService.createTraining(training);

        verify(trainingRepo, times(1)).createTraining(training);
    }

    @Test
    void testSelectTrainingFound() throws NoSuchTrainingException {
        when(trainingRepo.selectTraining(training.getId())).thenReturn(Optional.of(training));

        Training found = trainingService.selectTraining(training.getId());

        assertEquals(training.getId(), found.getId());
        assertEquals("Strength Training", found.getTrainingName());
    }

    @Test
    void testSelectTrainingNotFound() {
        UUID randomId = UUID.randomUUID();
        when(trainingRepo.selectTraining(randomId)).thenReturn(Optional.empty());

        assertThrows(NoSuchTrainingException.class,
                () -> trainingService.selectTraining(randomId));
    }
}