package gymapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private String spec;
    private boolean isActive;
    private List<TraineeProfile> traineeProfiles;
    public record TraineeProfile(
            String username,
            String firstName,
            String lastName
    ){}
}
