package architecture.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddUserRequest {
    @NotBlank
    private String name;

    @NotBlank
    private @Email String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
