package se306.team7.utility;

public class StopWatch implements IStopWatch {

    long _startTime;
    TimeUnit _unit;
    private final int MILLISECONDS_IN_SECOND = 1000;
    private final int SECONDS_IN_MINUTE = 60;
    private final int MINUTES_IN_HOUR = 60;

    /**
     * Create a new stop watch
     * @param unit Returns timed results in the given units
     */
    public StopWatch(TimeUnit unit) {
        _unit = unit;
    }

    /**
     * Start timing
     */
    public void Start() {
        _startTime = System.currentTimeMillis();
    }

    /**
     * Stops timing and returns the time difference between the most recent Start() call and now
     * @ Time difference in units specified by constructor
     */
    public double Stop() {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - _startTime;

        switch (_unit) {
            case Millisecond:
                return timeDifference;
            case Second:
                return timeDifference
                        * MILLISECONDS_IN_SECOND;
            case Minute:
                return timeDifference
                        * MILLISECONDS_IN_SECOND
                        * SECONDS_IN_MINUTE;
            case Hour:
                return timeDifference
                        * MILLISECONDS_IN_SECOND
                        * SECONDS_IN_MINUTE
                        * MINUTES_IN_HOUR;
        }

        return timeDifference;
    }

}
