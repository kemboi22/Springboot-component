package architecture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationTest {
    @Test
    void testMainShouldNotThrow() {
        Assertions.assertDoesNotThrow(() -> Application.main(new String[] { }));
    }
}
