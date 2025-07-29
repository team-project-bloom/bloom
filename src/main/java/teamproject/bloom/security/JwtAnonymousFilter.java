package teamproject.bloom.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAnonymousFilter extends OncePerRequestFilter {
    public static final String BEARER = "Bearer ";
    public static final List<GrantedAuthority> roles = List.of(
            new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        String username = null;
        if (token != null && jwtUtil.isValidToken(token)) {
            username = jwtUtil.getUsername(token);
        }
        if (token == null) {
            String jwtToken = jwtUtil.generatedToken();
            username = jwtUtil.getUsername(jwtToken);
            response.setHeader(HttpHeaders.AUTHORIZATION, jwtToken);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                roles
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }
}
