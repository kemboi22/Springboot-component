package architecture.auth;

import architecture.BaseControllerTest;
import architecture.Data;
import architecture.user.UserRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@MockBean(UserRepository.class)
class AuthControllerTest extends BaseControllerTest {
    @Autowired
    UserRepository userRepository;

    private static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.arguments(null, HttpStatus.INTERNAL_SERVER_ERROR),
            Arguments.arguments(new AuthRequest(null, null), HttpStatus.BAD_REQUEST),
            Arguments.arguments(new AuthRequest("", ""), HttpStatus.BAD_REQUEST),
            Arguments.arguments(new AuthRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString()), HttpStatus.BAD_REQUEST),
            Arguments.arguments(new AuthRequest(Data.user.getUsername(), UUID.randomUUID().toString()), HttpStatus.BAD_REQUEST),
            Arguments.arguments(new AuthRequest(UUID.randomUUID().toString(), Data.password), HttpStatus.BAD_REQUEST),
            Arguments.arguments(new AuthRequest(Data.user.getUsername(), Data.password), HttpStatus.OK)
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testPostShouldReturnHttpStatus(AuthRequest request, HttpStatusCode httpStatus) throws Exception {
        Mockito.when(userRepository.findByUsername(Data.user.getUsername())).thenReturn(Optional.of(Data.user));

        post("/auth", request).andExpectAll(MockMvcResultMatchers.status().is(httpStatus.value()));
    }
}
