package com.lsh.base.common.utils;

import java.util.Date;

/**
 * 日期提供者，使用它而不是直接取得系统时间，方便测试。
 *
 * @author calvin
 */
public interface ClockUtils {

    Date getCurrentDate();

    long getCurrentTimeInMillis();

    static final ClockUtils DEFAULT = new DefaultClock();

    /**
     * 默认时间提供者，返回当前的时间，线程安全。
     */
    public static class DefaultClock implements ClockUtils {

        public Date getCurrentDate() {
            return new Date();
        }

        public long getCurrentTimeInMillis() {
            return System.currentTimeMillis();
        }
    }

    /**
     * 可配置的时间提供者，用于测试.
     */
    public static class MockClock implements ClockUtils {

        private long time;

        public MockClock() {
            this(0);
        }

        public MockClock(Date date) {
            this.time = date.getTime();
        }

        public MockClock(long time) {
            this.time = time;
        }

        public Date getCurrentDate() {
            return new Date(time);
        }

        public long getCurrentTimeInMillis() {
            return time;
        }

        /**
         * 重新设置日期。
         */
        public void update(Date newDate) {
            time = newDate.getTime();
        }

        /**
         * 重新设置时间。
         */
        public void update(long newTime) {
            this.time = newTime;
        }

        /**
         * 滚动时间.
         */
        public void increaseTime(int millis) {
            time += millis;
        }

        /**
         * 滚动时间.
         */
        public void decreaseTime(int millis) {
            time -= millis;
        }
    }

}
