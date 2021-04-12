package fr.gdvd.media_manager.sec;

public interface SecurityParams {

    public static final String JWT_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String URL_SITE = "http://localhost";
    public static final String PRIVATE_SECRET = "direct?suprise,dir§mans.";
    public static final long EXPIRATION = 1000 * 60 * 60 * 24 *15;
        // millisec*1000 * s*60 * mn*60 * h*24 * d*15 = 15days
//    public static final long EXPIRATION = 1000 * 20; // Test with 20sec
}
