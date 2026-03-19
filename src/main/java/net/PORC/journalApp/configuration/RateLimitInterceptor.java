package net.PORC.journalApp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.PORC.journalApp.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String username = "guest";

        if (request.getUserPrincipal() != null) {
            username = request.getUserPrincipal().getName();
        }

        // unique key per user + endpoint
        String key = username + ":" + request.getRequestURI();

        if (!rateLimiterService.isAllowed(key)) {
            response.setStatus(429);
            response.setContentType("text/plain");
            response.getWriter().write("Too many actions. Slow down ⏳");
            return false;
        }

        return true;
    }
}
