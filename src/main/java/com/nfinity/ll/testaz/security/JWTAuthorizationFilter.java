package com.nfinity.ll.testaz.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nfinity.ll.testaz.commons.error.ApiError;
import com.nfinity.ll.testaz.commons.error.ExceptionMessageConstants;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import com.nfinity.ll.testaz.domain.model.JwtEntity;
import com.nfinity.ll.testaz.domain.irepository.IJwtRepository;
import java.net.URL;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    
    private IJwtRepository jwtRepo;
    public JWTAuthorizationFilter(AuthenticationManager authManager,ApplicationContext ctx) {
        super(authManager);
		this.jwtRepo = ctx.getBean(IJwtRepository.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        LoggingHelper logHelper = new LoggingHelper();
        try {
            authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
            return;

        } catch (ExpiredJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_EXPIRED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (UnsupportedJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_UNSUPPORTED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (MalformedJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_MALFORMED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (SignatureException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_INCORRECT_SIGNATURE);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (IllegalArgumentException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_ILLEGAL_ARGUMENT);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (JwtException exception) {
             apiError.setMessage(ExceptionMessageConstants.TOKEN_UNAUTHORIZED);
             logHelper.getLogger().error("An Exception Occurred:", exception);
             res.setStatus(401);
	    }


        OutputStream out = res.getOutputStream();
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiError);
        out.flush();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws JwtException {

        String token = request.getHeader(SecurityConstants.HEADER_STRING);
 
         // Check that the token is inactive in the JwtEntity table
         JwtEntity jwt = jwtRepo.findByToken(token);
         ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
 
         if(jwt == null) {
             throw new JwtException("Token Does Not Exist");
         }
        Claims claims;
       
		if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
        	String userName = null;
            List<GrantedAuthority> authorities = null;
            claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.SECRET.getBytes())
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getBody();
            userName = claims.getSubject();
            List<String> scopes = claims.get("scopes", List.class);
            authorities = scopes.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());
                        

        if ((userName != null) && StringUtils.isNotEmpty(userName)) {
        	return new UsernamePasswordAuthenticationToken(userName, null, authorities);
        }
        }
        return null;

    }

}