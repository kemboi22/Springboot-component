package architecture.auth;

import architecture.shared.exceptions.AppException;
import architecture.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String auth(final AuthRequest request) {
        final var user = userRepository.findByUsername(request.username());

        if (user.isEmpty() || !passwordEncoder.matches(request.password(), user.get().getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "errorAuthentication");
        }

        return jwtService.create(user.get());
    }
}
