import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapTest {

    private ForgettingMap<Integer, String> forgettingMap;

    @BeforeEach
    void setup() {
        forgettingMap = new ForgettingMap<>(5);
    }

    @Nested
    class TestAdd {

        @Test
        void testAddSingle() {
            forgettingMap.add(1, "Test");

            assertEquals(1, forgettingMap.size());

            final String str = forgettingMap.find(1);

            assertNotNull(str);
            assertEquals("Test", str);
        }

        @Test
        void testAddMultiple() {
            IntStream.range(1, 4).forEach(i -> forgettingMap.add(i, "Test" + i));

            assertEquals(3, forgettingMap.size());
            assertEquals("Test1", forgettingMap.find(1));
            assertEquals("Test2", forgettingMap.find(2));
            assertEquals("Test3", forgettingMap.find(3));
        }

        @Test
        void testAddDuplicate() {
            forgettingMap.add(1, "Test");
            forgettingMap.add(1, "Test2");

            assertEquals(1, forgettingMap.size());

            final String str = forgettingMap.find(1);
            assertNotEquals("Test", str);
            assertEquals("Test2", str);
        }

        @Test
        void testAddExcessive() {
            IntStream.range(1, 11).forEach(i -> forgettingMap.add(i, "Test" + i));

            assertEquals(5, forgettingMap.size());
            IntStream.range(1, 6).forEach(i -> assertNull(forgettingMap.find(i)));
            IntStream.range(6, 11).forEach(i -> assertNotNull(forgettingMap.find(i)));
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