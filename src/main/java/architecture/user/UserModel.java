package architecture.user;

import java.util.UUID;
import lombok.Data;

@Data
public class UserModel {
    private UUID id;
    private String name;
    private String email;
}
