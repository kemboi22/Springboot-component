package architecture.user;

import architecture.shared.swagger.DefaultApiResponses;
import architecture.shared.swagger.GetApiResponses;
import architecture.shared.swagger.PostApiResponses;
import architecture.user.requests.AddUserRequest;
import architecture.user.requests.GetUserRequest;
import architecture.user.requests.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get")
    @GetApiResponses
    @GetMapping
    public Page<UserModel> get(@ParameterObject @ModelAttribute @Valid final GetUserRequest request) {
        return userService.get(request);
    }

    @Operation(summary = "Get")
    @GetApiResponses
    @GetMapping("{id}")
    public UserModel get(@PathVariable final UUID id) {
        return userService.get(id);
    }

    @Operation(summary = "Add")
    @PostApiResponses
    @PostMapping
    public UUID add(@RequestBody @Valid final AddUserRequest request) {
        return userService.add(request);
    }

    @Operation(summary = "Update")
    @DefaultApiResponses
    @PutMapping("{id}")
    public void update(@PathVariable final UUID id, @RequestBody @Valid final UpdateUserRequest request) {
        request.setId(id);
        userService.update(request);
    }

    @Operation(summary = "Delete")
    @DefaultApiResponses
    @DeleteMapping("{id}")
    public void delete(@PathVariable final UUID id) {
        userService.delete(id);
    }
}
