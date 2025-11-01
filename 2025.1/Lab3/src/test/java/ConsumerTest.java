import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerTest {

    static class TestBuffer extends Buffer {
        private final int[] items;
        private int idx = 0;
        private final StringBuilder log = new StringBuilder();

        TestBuffer(int... items) {
            this.items = items;
        }

        @Override
        public void put(int value) {
            log.append("put:").append(value).append("\n");
        }

        @Override
        public int remove() {
            if (idx < items.length) {
                int v = items[idx++];
                log.append("remove:").append(v).append("\n");
                return v;
            }
            log.append("remove:-1\n");
            return -1;
        }

        String log() { return log.toString(); }
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldRequeueEvenItemForOddConsumer() {
        TestBuffer buf = new TestBuffer(2, -1);
        Consumer c = new Consumer(1, buf, 0);
        c.process(new Semaphore(1));
        String l = buf.log();
        assertTrue(l.contains("remove:2"));
        assertTrue(l.contains("put:2"));
    }

    @Test
    void shouldRequeueOddItemForEvenConsumer() {
        TestBuffer buf = new TestBuffer(3, -1);
        Consumer c = new Consumer(2, buf, 0);
        c.process(new Semaphore(1));
        String l = buf.log();
        assertTrue(l.contains("remove:3"));
        assertTrue(l.contains("put:3"));
    }

    @Test
    void shouldConsumeMatchingParityUntilSentinel() {
        TestBuffer buf = new TestBuffer(1, 3, 5, -1);
        Consumer c = new Consumer(1, buf, 0);
        c.process(new Semaphore(1));
        // Should not requeue any, only removes and stops at -1
        String l = buf.log();
        assertTrue(l.contains("remove:1"));
        assertTrue(l.contains("remove:3"));
        assertTrue(l.contains("remove:5"));
        assertTrue(l.contains("remove:-1"));
        assertFalse(l.contains("put:"));
    }

    @Test
    void shouldStopWhenSentinelEncounteredFirst() {
        TestBuffer buf = new TestBuffer(-1);
        Consumer c = new Consumer(1, buf, 0);
        c.process(new Semaphore(1));
        String l = buf.log();
        assertTrue(l.contains("remove:-1"));
    }

    @Test
    void shouldNotSleepWhenSleepTimeZero() {
        TestBuffer buf = new TestBuffer(1, -1);
        Consumer c = new Consumer(1, buf, 0);
        long start = System.currentTimeMillis();
        c.process(new Semaphore(1));
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed < 50, "Expected near-zero elapsed but was " + elapsed);
    }
}
