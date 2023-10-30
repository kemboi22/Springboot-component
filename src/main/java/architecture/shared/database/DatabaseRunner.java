package architecture.shared.database;

import architecture.auth.Authority;
import architecture.user.User;
import java.util.UUID;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DatabaseRunner implements ApplicationRunner {
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public void run(final ApplicationArguments args) {
        if (!mongoTemplate.collectionExists("users")) {
            mongoTemplate.save(new User(UUID.randomUUID(), "Admin", "admin@mail.com", "admin", passwordEncoder.encode("123456"), new Authority[] { Authority.ADMINISTRATOR }));
        }
    }
}
