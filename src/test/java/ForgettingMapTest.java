import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapTest {

    private ForgettingMap<Integer, String> forgettingMap = new ForgettingMap<>(5);

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
            IntStream.rangeClosed(1, 3).forEach(i -> forgettingMap.add(i, "Test" + i));

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
            IntStream.rangeClosed(1, 10).forEach(i -> forgettingMap.add(i, "Test" + i));

            assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());
            IntStream.rangeClosed(1, 5).forEach(i -> assertNull(forgettingMap.find(i)));
            IntStream.rangeClosed(6, 10).forEach(i -> assertNotNull(forgettingMap.find(i)));
        }

        @Test
        void testAddConcurrent() throws InterruptedException {
            final ExecutorService service = Executors.newFixedThreadPool(10);
            IntStream.rangeClosed(1, 50).forEach(i -> service.execute(() -> forgettingMap.add(i, "Test" + i)));

            service.shutdown();

            if (service.awaitTermination(10L, TimeUnit.SECONDS)) {
                assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());

            } else {
                fail("Failed to execute all threads in time, please look at fixing this");
            }

        }
    }

    @Nested
    class TestFind {

        @BeforeEach
        void setup() {
            forgettingMap.add(1, "Test");
        }

        @Test
        void testFind() {
            final String val = forgettingMap.find(1);

            assertNotNull(val);
            assertEquals("Test", val);
        }

        @Test
        void testFindInvalid() {
            final String val = forgettingMap.find(2);

            assertNull(val);
        }
    }
}