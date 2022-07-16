package me.kqlqk.todo_list.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableAspectJAutoProxy
public class Config extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final String[] urlsForGuests = {
            "/",
            "/login",
            "/registration",
            "/error",
            "/recovery",
            "/recovery/{\\d}",
            "/tempOAuth2LoginPage"};

    private final String[] urlsForUser = {
            "/home", "/home/",
            "/home/{\\d}", "/home/{\\d}/",
            "/home/{\\d}/new", "/home/{\\d}/new/",
            "/home/{\\d}/edit", "/home/{\\d}/edit/"};

    private final String[] urlsForAdmins = {
            "/admin", "/admin/"};

    @Autowired
    public Config(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(urlsForGuests).permitAll()
                    .antMatchers(urlsForUser).hasAnyRole("USER", "ADMIN")
                    .antMatchers(urlsForAdmins).hasRole("ADMIN")
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/tempOAuth2LoginPage")
                    .failureUrl("/login")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
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

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}