package in.getdreamjob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests(auth -> {
                    auth.antMatchers(HttpMethod.POST, "/api/v1/**").hasAnyRole("ADMIN");
                    auth.antMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("ADMIN");
                    auth.antMatchers(HttpMethod.DELETE, "/api/v1/**").hasAnyRole("ADMIN");
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("arvind")
                .password("Asdlkj46&")
                .roles("ADMIN").build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("gautam")
                .password("Asdlkj46")
                .roles("ADMIN").build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://onflydreamjob.com", "https://www.onflydreamjob.com",
                "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET"));
        configuration.setAllowedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
}
