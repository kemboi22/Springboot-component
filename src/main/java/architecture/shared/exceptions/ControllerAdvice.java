package architecture.shared.exceptions;

import architecture.shared.services.MessageService;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final MessageService messageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handle(final AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handle(final NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handle(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageService.get("errorBinding", exception.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(final MethodArgumentNotValidException exception) {
        Function<MessageSourceResolvable, String> map = error -> {
            if (error instanceof FieldError fieldError) {
                if (fieldError.isBindingFailure()) {
                    return messageService.get("errorBinding", fieldError.getField());
                } else {
                    return StringUtils.capitalize(String.format("%s: %s.", fieldError.getField(), error.getDefaultMessage()));
                }
            }

            return StringUtils.capitalize(error.getDefaultMessage());
        };

        final var body = exception.getBindingResult().getAllErrors().stream().map(map).collect(Collectors.joining(" "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handle(final AppException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }
}
