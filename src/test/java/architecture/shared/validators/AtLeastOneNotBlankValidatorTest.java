package architecture.shared.validators;

import jakarta.validation.Validation;
import java.util.stream.Stream;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AtLeastOneNotBlankValidatorTest {
    private static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.arguments(new AtLeastOneNotBlankValidatorTestExample(null, null), false),
            Arguments.arguments(new AtLeastOneNotBlankValidatorTestExample("", ""), false),
            Arguments.arguments(new AtLeastOneNotBlankValidatorTestExample("value", ""), true),
            Arguments.arguments(new AtLeastOneNotBlankValidatorTestExample("", "value"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testAtLeastOneNotBlank(AtLeastOneNotBlankValidatorTestExample value, boolean expected) {
        Assertions.assertEquals(expected, Validation.buildDefaultValidatorFactory().getValidator().validate(value).isEmpty());
    }
}

@Data
@AtLeastOneNotBlank(fields = { "field1", "field2" })
class AtLeastOneNotBlankValidatorTestExample {
    private String field1;
    private String field2;

    AtLeastOneNotBlankValidatorTestExample(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
}
