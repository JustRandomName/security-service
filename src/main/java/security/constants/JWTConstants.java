package security.constants;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * Constants for work with token
 * */
public final class JWTConstants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String UNABLE = "Unable to get JWT Token";
    public static final String EXPIRED = "JWT Token has expired";
    public static final String HEADER = "Authorization";
    public static final String NOT_BEARER = "JWT Token does not begin with Bearer String";

    protected JWTConstants() {
    }
}
