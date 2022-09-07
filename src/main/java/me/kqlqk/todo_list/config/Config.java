package me.kqlqk.todo_list.config;

import me.kqlqk.todo_list.config.filters.JWTFilter;
import me.kqlqk.todo_list.config.filters.RequestRejectedExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {
    private final JWTFilter jwtFilter;
    private final RequestRejectedExceptionFilter requestRejectedExceptionFilter;
    private final UserDetailsService userDetailsService;

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
    public Config(JWTFilter jwtFilter,
                  RequestRejectedExceptionFilter requestRejectedExceptionFilter,
                  UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.requestRejectedExceptionFilter = requestRejectedExceptionFilter;
        this.userDetailsService = userDetailsService;
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
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}