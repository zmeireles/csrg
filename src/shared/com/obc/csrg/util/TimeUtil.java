package com.obc.csrg.util;

import java.io.Serializable;

public final class TimeUtil implements Serializable {

	private static final long serialVersionUID = 200810141859L;
    private TimeUtil() { }

    public static final long MILLIS_IN_1_SECOND   = 1000L;
    public static final long MILLIS_IN_1_MINUTE   =   60L * MILLIS_IN_1_SECOND;
    public static final long MILLIS_IN_5_MINUTES  =    5L * MILLIS_IN_1_MINUTE;
    public static final long MILLIS_IN_15_MINUTES =    3L * MILLIS_IN_5_MINUTES;
    public static final long MILLIS_IN_30_MINUTES =    2L * MILLIS_IN_15_MINUTES;
    public static final long MILLIS_IN_1_HOUR     =    2L * MILLIS_IN_30_MINUTES;
    public static final long MILLIS_IN_1_DAY      =   24L * MILLIS_IN_1_HOUR;
}
