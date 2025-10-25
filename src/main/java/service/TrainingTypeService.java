package service;

import dto.response.TrainingTypeResponse;
import entity.TrainingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.TrainingTypeMapper;
import org.springframework.stereotype.Service;
import repository.TrainingTypeRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeService {
    private final TrainingTypeRepo trainingTypeRepo;
    private final TrainingTypeMapper mapper;

    public List<TrainingTypeResponse> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepo.selectAll();
        List<TrainingTypeResponse> response = new ArrayList<>();
        for (TrainingType t : trainingTypes) {
            response.add(mapper.toResponse(t));
        }
        log.debug("Fetched all training types successfully");
        return response;
    }
}
