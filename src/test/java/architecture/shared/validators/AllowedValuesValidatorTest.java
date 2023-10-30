package architecture.shared.validators;

import jakarta.validation.Validation;
import java.util.stream.Stream;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AllowedValuesValidatorTest {
    private static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.arguments(new AllowedValuesValidatorTestExample("1"), false),
            Arguments.arguments(new AllowedValuesValidatorTestExample("not allowed"), false),
            Arguments.arguments(new AllowedValuesValidatorTestExample("2"), true),
            Arguments.arguments(new AllowedValuesValidatorTestExample("allowed"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testAllowedValues(AllowedValuesValidatorTestExample value, boolean expected) {
        Assertions.assertEquals(expected, Validation.buildDefaultValidatorFactory().getValidator().validate(value).isEmpty());
    }
}

@Data
@AllowedValues(field = "field", values = { "2", "allowed" })
class AllowedValuesValidatorTestExample {
    private String field;

    AllowedValuesValidatorTestExample(String field) {
        this.field = field;
    }
}
