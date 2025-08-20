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
import org.springframework.web.filter.OncePerRequestFilter;
import teamproject.bloom.mapper.UserMapper;
import teamproject.bloom.model.User;
import teamproject.bloom.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtAnonymousFilter extends OncePerRequestFilter {
    public static final List<GrantedAuthority> roles = List.of(
            new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        if (token != null && jwtUtil.isValidToken(token)) {
            String username = jwtUtil.getUsername(token);
            User user = userMapper.createUser(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    roles
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return userService.getToken(bearerToken);
    }
}
