package security.config;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static security.constants.JWTConstants.UNAUTHORIZED;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@Component
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {

        response.sendError(SC_UNAUTHORIZED, UNAUTHORIZED);
    }
}
