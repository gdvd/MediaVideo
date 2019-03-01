package fr.gdvd.media_manager.configuration;

public interface SecurityParams {

    public static final String JWT_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String PRIVATE_SECRET = "gdvd@me.com";
    public static final long EXPIRATION = 10*24*3600;
}