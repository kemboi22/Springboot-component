package architecture.user;

import architecture.auth.Authority;
import architecture.shared.exceptions.AppException;
import architecture.shared.services.MapperService;
import architecture.shared.services.MessageService;
import architecture.user.requests.AddUserRequest;
import architecture.user.requests.GetUserRequest;
import architecture.user.requests.UpdateUserRequest;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final MapperService mapperService;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public Page<UserModel> get(final GetUserRequest request) {
        final var entities = userRepository.findAll(request.getExample(User.class), request.getPageable());

        if (entities.isEmpty()) throw new NoSuchElementException();

        return mapperService.map(entities, UserModel.class);
    }

    public UserModel get(final UUID id) {
        final var entity = userRepository.findById(id).orElseThrow();

        return mapperService.map(entity, UserModel.class);
    }

    public UUID add(final AddUserRequest request) {
        final var exists = userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername());

        if (exists) throw new AppException(HttpStatus.CONFLICT, messageService.get("errorUnique"));

        var entity = mapperService.map(request, User.class);

        entity.setId(UUID.randomUUID());

        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        entity.setAuthorities(new Authority[] { Authority.DEFAULT });

        entity = userRepository.insert(entity);

        return entity.getId();
    }

    public void update(final UpdateUserRequest request) {
        final var exists = userRepository.existsByEmailOrUsername(request.getId(), request.getEmail(), request.getUsername());

        if (exists) throw new AppException(HttpStatus.CONFLICT, messageService.get("errorUnique"));

        var entity = mapperService.map(request, User.class);

        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(entity);
    }

    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }
}
