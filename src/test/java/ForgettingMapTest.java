import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ForgettingMapTest {

    private final ForgettingMap<Integer, String> forgettingMap = new ForgettingMap<>(5);
    private final ExecutorService                service       = Executors.newFixedThreadPool(10);

    @Nested
    class TestAdd {

        @Test
        void testSingle() {
            addToForgettingMap(1);

            assertEquals(1, forgettingMap.size());

            final String str = forgettingMap.find(1);

            assertNotNull(str);
            assertEquals("Test1", str);
        }

        @Test
        void testMultiple() {
            addToForgettingMap(3);

            assertEquals(3, forgettingMap.size());
            assertEquals("Test1", forgettingMap.find(1));
            assertEquals("Test2", forgettingMap.find(2));
            assertEquals("Test3", forgettingMap.find(3));
        }

        @Test
        void testDuplicate() {
            forgettingMap.add(1, "Test");
            forgettingMap.add(1, "Test2");

            assertEquals(1, forgettingMap.size());

            final String str = forgettingMap.find(1);
            assertNotEquals("Test", str);
            assertEquals("Test2", str);
        }

        @Test
        void testExcessive() {
            addToForgettingMap(10);
            IntStream.rangeClosed(1, 10).forEach(i -> forgettingMap.add(i, "Test" + i));

            assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());
            IntStream.rangeClosed(1, 5).forEach(i -> assertNull(forgettingMap.find(i)));
            IntStream.rangeClosed(6, 10).forEach(i -> assertNotNull(forgettingMap.find(i)));
        }

        @Test
        void testConcurrent() throws InterruptedException {
            IntStream.rangeClosed(1, 50).forEach(i -> service.execute(() -> forgettingMap.add(i, "Test" + i)));

            service.shutdown();

            if (service.awaitTermination(10L, TimeUnit.SECONDS)) {
                assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());

            } else {
                fail("Failed to execute all threads in time, please look at fixing this");
            }
        }

        @Test
        void testRemovalSimple() throws InterruptedException {
            addToForgettingMap(forgettingMap.getMaxSize());

            service.execute(() -> forgettingMap.find(1));
            service.execute(() -> forgettingMap.find(2));
            service.execute(() -> forgettingMap.find(3));
            service.execute(() -> forgettingMap.find(4));

            service.shutdown();

            if (service.awaitTermination(10L, TimeUnit.SECONDS)) {
                forgettingMap.add(10, "Test10");

                assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());

                assertNotNull(forgettingMap.find(10)); // Check new entry was added

                assertNotNull(forgettingMap.find(1)); // Check existing entry maintained
                assertNotNull(forgettingMap.find(2)); // Check existing entry maintained
                assertNotNull(forgettingMap.find(3)); // Check existing entry maintained
                assertNotNull(forgettingMap.find(4)); // Check existing entry maintained

                assertNull(forgettingMap.find(5)); // Check key with no find() is removed

            } else {
                fail("Failed to execute all threads in time, please look at fixing this");
            }
        }

        /**
         * Tests the removal action when there are 1 or more entries with the same `find()` frequency.
         *
         * Should remove the one that was first added.
         */
        @Test
        void testRemovalDuplicate() {
            addToForgettingMap(forgettingMap.getMaxSize());

            forgettingMap.find(1);
            forgettingMap.find(2);
            forgettingMap.find(3);

            forgettingMap.add(10, "Test10");

            assertEquals(forgettingMap.getMaxSize(), forgettingMap.size());
            assertNotNull(forgettingMap.find(10)); // Check new entry was added

            assertNotNull(forgettingMap.find(1)); // Check existing entry maintained
            assertNotNull(forgettingMap.find(2)); // Check existing entry maintained
            assertNotNull(forgettingMap.find(3)); // Check existing entry maintained
            assertNotNull(forgettingMap.find(5)); // Check existing later entry maintained

            assertNull(forgettingMap.find(4)); // Check that earlier entry is removed
        }

        private void addToForgettingMap(final int rangeEndInclusive) {
            IntStream.rangeClosed(1, rangeEndInclusive).forEach(i -> forgettingMap.add(i, "Test" + i));
        }
    }

    @Nested
    class TestFind {

        @BeforeEach
        void setup() {
            forgettingMap.add(1, "Test");
        }

        @Test
        void testSingle() {
            final int times = 1;
            final String val = forgettingMap.find(times);

            assertNotNull(val);
            assertEquals("Test", val);
            assertEquals(times, forgettingMap.getEntryFrequency(1));
        }

        @Test
        void testInvalid() {
            final String val = forgettingMap.find(2);

            assertNull(val);
        }

        @Test
        void testConcurrent() throws InterruptedException {
            final int times = 20;
            IntStream.rangeClosed(1, times).forEach(i -> service.execute(() -> forgettingMap.find(1)));

            service.shutdown();

            if (service.awaitTermination(10L, TimeUnit.SECONDS)) {
                assertEquals(1, forgettingMap.size());
                assertEquals(times, forgettingMap.getEntryFrequency(1));

            } else {
                fail("Failed to execute all threads in time, please look at fixing this");
            }
        }
    }
}