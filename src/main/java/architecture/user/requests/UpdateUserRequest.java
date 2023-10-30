package architecture.user.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @JsonIgnore
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private @Email String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
