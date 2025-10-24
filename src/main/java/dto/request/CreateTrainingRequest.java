package dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Date trainingDate;
    private int trainingDuration;
}
