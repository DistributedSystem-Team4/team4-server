package Distributed.System.team4server.configuration;

import Distributed.System.team4server.service.UserService;
import Distributed.System.team4server.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;
    private static final Logger log = LoggerFactory.getLogger(Log.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 'Bearer + 토큰' 안보내면 블락
        if(authorization==null || !authorization.startsWith("Bearer") ){
            filterChain.doFilter(request,response);
            return;
        }

        //토큰꺼내기
        String token = authorization.split(" ")[1];
        log.info(authorization);
        log.info(authorization.split(" ")[1]);

        //토큰 만료여부
        if(JwtUtil.isExpired(token,secretKey)){
            filterChain.doFilter(request,response);
            return;
        }

        //토큰에서 id거내기
        String userId = JwtUtil.getUserId(token,secretKey);
        log.info(userId);
        //권한부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
