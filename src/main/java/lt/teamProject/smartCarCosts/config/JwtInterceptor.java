package lt.teamProject.smartCarCosts.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lt.teamProject.smartCarCosts.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;


        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }


        if (token != null) {
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            if (decodedJWT != null) {

                request.getSession().setAttribute("userId", decodedJWT.getClaim("userId").asLong());
                request.getSession().setAttribute("userName", decodedJWT.getClaim("userName").asString());
                request.getSession().setAttribute("userEmail", decodedJWT.getClaim("email").asString());
                return true;
            }
        }


        response.sendRedirect("/login");
        return false;
    }
}