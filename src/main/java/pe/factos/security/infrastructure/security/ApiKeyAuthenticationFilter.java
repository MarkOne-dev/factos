package pe.factos.security.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.factos.security.domain.repositories.ApiKeyRepository;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    public static final String API_KEY_HEADER = "X-API-KEY";
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthenticationFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/swagger-ui") || requestPath.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKeyHeaderValue = request.getHeader(API_KEY_HEADER);
        if (apiKeyHeaderValue != null && !apiKeyHeaderValue.isBlank()) {
            var apiKeyOpt = apiKeyRepository.findByKeyValue(apiKeyHeaderValue);
            if (apiKeyOpt.isPresent() && apiKeyOpt.get().isValid()) {
                var auth = new UsernamePasswordAuthenticationToken(
                        apiKeyOpt.get().getClientName(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
