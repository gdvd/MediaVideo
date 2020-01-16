package fr.gdvd.media_manager.sec;

public interface SecurityParams {

    public static final String JWT_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String PRIVATE_SECRET = "writeYourOwnPassword";
    public static final long EXPIRATION = 1000 * 60 * 60 * 36; // millisec*1000 * s*60 * mn*60 * h*36 = 36h
}
