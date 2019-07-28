package fr.gdvd.media_manager.sec;

public interface SecurityParams {

    public static final String JWT_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String PRIVATE_SECRET = "gdvd@me.com";
    public static final long EXPIRATION = 1000 * 60 * 60 * 18;//1000*60*60*12;// millisec-s-mn-h = 18h
}
