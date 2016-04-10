package utils;

public enum GPSTrackerErrorType {

    LACK_OF_TRACK_FILE("Track file isn't available yet!"),
    LACK_OF_IMAGE_FILE("Image isn't available yet!");

    private String name;

    GPSTrackerErrorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
