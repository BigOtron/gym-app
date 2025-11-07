package gymapp.service;

import gymapp.dto.response.TrainingTypeResponse;
import gymapp.entity.TrainingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import gymapp.mapper.TrainingTypeMapper;
import org.springframework.stereotype.Service;
import gymapp.repository.TrainingTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepo;
    private final TrainingTypeMapper mapper;

    public List<TrainingTypeResponse> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepo.findAll();
        List<TrainingTypeResponse> response = new ArrayList<>();
        for (TrainingType t : trainingTypes) {
            response.add(mapper.toResponse(t));
        }
        log.debug("Fetched all training types successfully");
        return response;
    }
}
