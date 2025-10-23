package dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTrainerProfileRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String spec;
    private boolean isActive;
}
