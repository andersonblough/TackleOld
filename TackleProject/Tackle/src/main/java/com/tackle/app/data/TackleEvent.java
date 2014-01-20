package com.tackle.app.data;

/**
 * Created by Bill on 1/19/14.
 */
public class TackleEvent {

    public static final class Type{

        /**
         * default types of Tackle Events
         */
        public static final int TODO = 1;
        public static final int LIST = 2;
        public static final int NOTE = 3;
        public static final int EVENT = 4;

    }

    public static final class Frequency{
        /**
         * default values for frequency
         */
        public static final int DAILY = 1;
        public static final int WEEKLY = 2;
        public static final int MONTHLY = 3;
        public static final int YEARLY = 4;

    }

    public static final class Status{
        /**
         * default values for status
         */
        public static final int ONGOING = 0;
        public static final int TACKLED = 1;
    }

}
