package gymapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsResponse {
    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private int trainingDuration;
    private String trainerName;
}
