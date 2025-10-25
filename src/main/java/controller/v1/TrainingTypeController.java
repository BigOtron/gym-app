package controller.v1;

import dto.response.TrainingTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/training-type", produces = "application/json")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping("/")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> response = trainingTypeService.getAllTrainingTypes();
        return ResponseEntity.ok(response);
    }
}
