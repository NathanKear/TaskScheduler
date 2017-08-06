package se306.team7.utility;

public class StopWatch implements IStopWatch {

    long _startTime;
    TimeUnit _unit;
    private final int MILLISECONDS_IN_SECOND = 1000;
    private final int SECONDS_IN_MINUTE = 60;
    private final int MINUTES_IN_HOUR = 60;

    public StopWatch(TimeUnit unit) {
        _unit = unit;
    }

    public void Start() {
        _startTime = System.currentTimeMillis();
    }

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
