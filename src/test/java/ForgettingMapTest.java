import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ForgettingMapTest {

    private ForgettingMap forgettingMap;

    @BeforeAll
    void setup() {
        forgettingMap = new ForgettingMap();
    }

    @Nested
    class TestAdd {

        @Test
        void testAddSingle() {
            assertFalse(true);
        }
    }

    @Nested
    class TestFind {

        @Test
        void testFind() {
            assertFalse(true);
        }
    }
}