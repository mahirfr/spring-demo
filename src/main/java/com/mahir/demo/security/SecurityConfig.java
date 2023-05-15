package com.mahir.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtFilter filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(myUserDetailsService);

        // Manually adding users in Memory (RAM)
//        auth
//                .inMemoryAuthentication()
//                .withUser("mahir")
//                .password("root")
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password("root")
//                .roles("ADMIN");


        // DB authentication
//        auth    .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("SELECT email, password, 1 FROM user WHERE email = ?")
//                .authoritiesByUsernameQuery(
//                        " SELECT email, IF(admin,'ROLE_ADMIN','ROLE_USER') " +
//                                " FROM user" +
//                                " WHERE email = ?"
//                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Controls access to pages
        http.cors().configurationSource(httpServletRequest -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.applyPermitDefaultValues();
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("X-Requested-With", "Origin", "Content-Type",
                                    "Accept", "Authorization","Access-Control-Allow-Origin"));
                    return corsConfiguration;
                })
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/signup", "/").permitAll()   // anybody can have access to the login page
                .antMatchers("/admin/**").hasRole("ADMIN")      // the url that contains admin can only be accessed by admins
                .antMatchers("/**").hasAnyRole("ADMIN", "USER") // the rest of the pages can be accessed by any logged-in user
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
