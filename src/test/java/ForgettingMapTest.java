import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapTest {

    private ForgettingMap forgettingMap;

    @BeforeEach
    void setup() {
        forgettingMap = new ForgettingMap(5);
    }

    @Nested
    class TestAdd {

        @Test
        void testAddSingle() {
            fail();
        }

        @Test
        void testAddMultiple() {
            fail();
        }

        @Test
        void testAddExcessive() {
            fail();
        }

        @Test
        void testAddConcurrent() {
            fail();
        }
    }

    @Nested
    class TestFind {

        @Test
        void testFind() {
            fail();
        }

        @Test
        void testFindInvalid() {
            fail();
        }
    }
}