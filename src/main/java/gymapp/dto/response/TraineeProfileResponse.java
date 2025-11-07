package gymapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerProfile> trainerProfiles;
    public record TrainerProfile(
            String username,
            String firstName,
            String lastName,
            String spec
    ) {}
}
