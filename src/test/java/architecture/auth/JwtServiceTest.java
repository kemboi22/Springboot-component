package architecture.auth;

import architecture.Data;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JwtService.class)
class JwtServiceTest {
    @Autowired
    JwtService jwtService;

    @ParameterizedTest
    @NullAndEmptySource
    void testVerifyShouldReturnFalse(String token) {
        Assertions.assertFalse(jwtService.verify(token));
    }

    @Test
    void testVerifyShouldReturnFalse() {
        Assertions.assertFalse(jwtService.verify(UUID.randomUUID().toString()));
    }

    @Test
    void testVerifyShouldReturnTrue() {
        Assertions.assertTrue(jwtService.verify(jwtService.create(Data.user)));
    }
}
