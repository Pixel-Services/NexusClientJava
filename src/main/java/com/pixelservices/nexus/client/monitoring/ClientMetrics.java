package com.pixelservices.nexus.client.monitoring;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Metrics collection for the Nexus client.
 * Tracks request counts, response times, and error rates.
 */
public class ClientMetrics {
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private final LocalDateTime startTime = LocalDateTime.now();

    /**
     * Records a successful request.
     *
     * @param responseTimeMs the response time in milliseconds
     */
    public void recordSuccess(long responseTimeMs) {
        totalRequests.incrementAndGet();
        successfulRequests.incrementAndGet();
        totalResponseTime.addAndGet(responseTimeMs);
    }

    /**
     * Records a failed request.
     *
     * @param responseTimeMs the response time in milliseconds
     */
    public void recordFailure(long responseTimeMs) {
        totalRequests.incrementAndGet();
        failedRequests.incrementAndGet();
        totalResponseTime.addAndGet(responseTimeMs);
    }

    /**
     * Gets the total number of requests made.
     *
     * @return the total request count
     */
    public long getTotalRequests() {
        return totalRequests.get();
    }

    /**
     * Gets the number of successful requests.
     *
     * @return the successful request count
     */
    public long getSuccessfulRequests() {
        return successfulRequests.get();
    }

    /**
     * Gets the number of failed requests.
     *
     * @return the failed request count
     */
    public long getFailedRequests() {
        return failedRequests.get();
    }

    /**
     * Gets the success rate as a percentage.
     *
     * @return the success rate (0.0 to 100.0)
     */
    public double getSuccessRate() {
        long total = totalRequests.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) successfulRequests.get() / total * 100.0;
    }

    /**
     * Gets the average response time in milliseconds.
     *
     * @return the average response time
     */
    public double getAverageResponseTime() {
        long total = totalRequests.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) totalResponseTime.get() / total;
    }

    /**
     * Gets the client uptime in seconds.
     *
     * @return the uptime in seconds
     */
    public long getUptimeSeconds() {
        return java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
    }

    /**
     * Resets all metrics.
     */
    public void reset() {
        totalRequests.set(0);
        successfulRequests.set(0);
        failedRequests.set(0);
        totalResponseTime.set(0);
    }

    @Override
    public String toString() {
        return String.format(
            "ClientMetrics{totalRequests=%d, successfulRequests=%d, failedRequests=%d, " +
            "successRate=%.2f%%, avgResponseTime=%.2fms, uptime=%ds}",
            getTotalRequests(), getSuccessfulRequests(), getFailedRequests(),
            getSuccessRate(), getAverageResponseTime(), getUptimeSeconds()
        );
    }
}
