package com.book.common.snowflake;

public class Snowflake {

    private final long nodeId;
    private final static long EPOCH = 1640995200000L; // 2022-01-01 00:00:00 UTC

    private final static long NODE_ID_BITS = 10L;
    private final static long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;

    private final static long SEQUENCE_BITS = 12L;
    private final static long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private final static long NODE_ID_SHIFT = SEQUENCE_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public Snowflake(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(String.format("Node ID must be between 0 and %d", MAX_NODE_ID));
        }
        this.nodeId = nodeId;
    }

    public synchronized long generateId() {
        long currentTimestamp = currentTime();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // Sequence overflow in same millisecond
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (nodeId << NODE_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = currentTime();
        }
        return currentTimestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
