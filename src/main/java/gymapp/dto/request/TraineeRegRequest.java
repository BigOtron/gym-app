package gymapp.dto.request;

import gymapp.utility.annotation.ValidDateOfBirth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "2 <= first name <= 50")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min=2, max=50, message = "2 <= last name <= 50")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @ValidDateOfBirth
    private Date dateOfBirth;

    @NotBlank(message = "Address is required")
    @Size(min = 8, max = 255, message = "8 <= address <= 255")
    private String address;
}
