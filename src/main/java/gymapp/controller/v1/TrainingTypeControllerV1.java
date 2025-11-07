package gymapp.controller.v1;

import gymapp.dto.response.TrainingTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gymapp.service.TrainingTypeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/training-type", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "TrainingType", description = "Operations related to training type management")
public class TrainingTypeControllerV1 {
    private final TrainingTypeService trainingTypeService;

    @GetMapping("/")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        log.info("Fetching all training types...");
        List<TrainingTypeResponse> response = trainingTypeService.getAllTrainingTypes();
        log.info("Fetched {} training types successfully.", response.size());
        return ResponseEntity.ok(response);
    }
}
