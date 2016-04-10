package utils;

public class GPSTrackerException extends Exception{

    public GPSTrackerException(String name){
        super(name);
    }

    public GPSTrackerException(GPSTrackerErrorType errorType){
        super(errorType.getName());
    }

}
