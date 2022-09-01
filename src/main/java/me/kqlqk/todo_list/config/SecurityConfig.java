package me.kqlqk.todo_list.config;

import me.kqlqk.todo_list.config.filters.JWTFilter;
import me.kqlqk.todo_list.config.filters.RequestRejectedExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JWTFilter jwtFilter;
    private final RequestRejectedExceptionFilter requestRejectedExceptionFilter;

    public static final String[] urlsForGuests = {
            "/",
            "/login",
            "/registration",
            "/error",
            "/recovery/**",
            "/api/login",
            "/api/registration",
            "/api/error",
            "/api/recovery/**"};

    public static final String[] urlsForUser = {
            "/home/**",
            "/api/**",
            "/logout"};

    public static final String[] urlsForAdmins = {
            "/admin/**",
            "/api/admin/**"};


    @Autowired
    public SecurityConfig(DaoAuthenticationProvider daoAuthenticationProvider, JWTFilter jwtFilter, RequestRejectedExceptionFilter requestRejectedExceptionFilter) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.jwtFilter = jwtFilter;
        this.requestRejectedExceptionFilter = requestRejectedExceptionFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(urlsForGuests).permitAll()
                        .antMatchers(urlsForUser).hasAnyRole("USER", "ADMIN")
                        .antMatchers(urlsForAdmins).hasRole("ADMIN")
                .and()
                    .addFilterBefore(requestRejectedExceptionFilter, WebAsyncManagerIntegrationFilter.class)
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("at", "rt")
                        .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider);
    }

}