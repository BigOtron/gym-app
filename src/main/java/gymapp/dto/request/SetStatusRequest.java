package gymapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SetStatusRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotNull(message = "isActive must be provided")
    private boolean isActive;
}
