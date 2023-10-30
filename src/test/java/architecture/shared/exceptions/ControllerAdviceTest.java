package architecture.shared.exceptions;

import architecture.shared.services.MessageService;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@SpringBootTest(classes = { ControllerAdvice.class, MessageService.class })
class ControllerAdviceTest {
    @Autowired
    ControllerAdvice controllerAdvice;

    @Autowired
    MessageService messageService;

    @Test
    void testHandleExceptionShouldReturnInternalServerError() {
        final var exception = new Exception();

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testHandleAccessDeniedExceptionShouldReturnUnauthorized() {
        final var exception = new AccessDeniedException("AccessDeniedException");

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testHandleNoSuchElementExceptionShouldReturnNotFound() {
        final var exception = new NoSuchElementException();

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHandleMethodArgumentTypeMismatchExceptionShouldReturnBadRequest() throws NoSuchMethodException {
        final var parameter = new MethodParameter(ControllerAdviceTest.class.getMethod("toString"), -1);

        final var exception = new MethodArgumentTypeMismatchException("X", Integer.class, "Id", parameter, new Exception());

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Assertions.assertEquals(messageService.get("errorBinding", exception.getName()), response.getBody());
    }

    @Test
    void testHandleMethodArgumentNotValidExceptionShouldReturnBadRequest() throws NoSuchMethodException {
        final var parameter = new MethodParameter(ControllerAdviceTest.class.getMethod("toString"), -1);

        final var binding = Mockito.mock(BindingResult.class);

        final var errors = new ArrayList<FieldError>();

        errors.add(new FieldError("object", "field", "error"));

        Mockito.when(binding.getFieldErrors()).thenReturn(errors);

        Mockito.when(binding.hasErrors()).thenReturn(true);

        final var exception = new MethodArgumentNotValidException(parameter, binding);

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleAppExceptionShouldReturnBadRequest() {
        final var exception = new AppException(HttpStatus.BAD_REQUEST);

        final var response = controllerAdvice.handle(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
