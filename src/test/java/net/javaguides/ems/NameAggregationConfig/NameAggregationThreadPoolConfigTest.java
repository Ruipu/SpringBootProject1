package net.javaguides.ems.NameAggregationConfig;


import net.javaguides.ems.NameAggregation.config.ThreadPoolConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolConfigTest {

    private final ThreadPoolConfig config = new ThreadPoolConfig();

    @Test
    @DisplayName("taskExecutor - should return configured executor")
    void taskExecutor_Success() {
        Executor executor = config.taskExecutor();

        assertNotNull(executor);
        assertInstanceOf(ThreadPoolTaskExecutor.class, executor);

        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
        assertEquals(5, taskExecutor.getCorePoolSize());
        assertEquals(10, taskExecutor.getMaxPoolSize());
        assertEquals("Async-", taskExecutor.getThreadNamePrefix());
    }
}