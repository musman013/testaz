package com.nfinity.ll.testaz;

import org.springframework.core.env.Environment; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.nfinity.ll.testaz.security.JWTAuthenticationFilter;
import com.nfinity.ll.testaz.security.JWTAuthorizationFilter;

import static com.nfinity.ll.testaz.security.SecurityConstants.CONFIRM;
import static com.nfinity.ll.testaz.security.SecurityConstants.REGISTER;
import javax.naming.AuthenticationNotSupportedException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
	private Environment env;

	@Autowired
    private ApplicationContext context;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

      /*   if (env.getProperty("fastCode.auth.method").equalsIgnoreCase("oidc") ) {
            // The following configuration is for SSO
           http
                    .cors()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/actuator/**","/configuration/ui", "/swagger-resources", "/configuration/security", "/v3/api-docs/", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/browser/index.html#", "/browser/**").permitAll()
                    .antMatchers(HttpMethod.POST, REGISTER).permitAll()
                    .antMatchers(HttpMethod.POST, CONFIRM).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login();

        }

        // The following authorization configuration is for database/LDAP

        else {*/

            http
                    .cors()
                    .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/actuator/**", "/configuration/ui", "/swagger-resources", "/configuration/security", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/browser/index.html#", "/browser/**").permitAll()
                    .antMatchers(HttpMethod.POST, REGISTER).permitAll()
                    .antMatchers(HttpMethod.POST, CONFIRM).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(new JWTAuthenticationFilter(authenticationManager(),context))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(),context));
      //  }
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        if(env.getProperty("fastCode.auth.method").equalsIgnoreCase("database")) {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
        else {
            throw new AuthenticationNotSupportedException();
        }
    }
}
