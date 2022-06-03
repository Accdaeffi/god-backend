package ru.ifmo.mpi.magichospital.god.conf;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ru.ifmo.mpi.magichospital.god.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests().anyRequest().authenticated()
			.and().httpBasic()
			.and().cors();
    }
	
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService);
    }
    
	@Bean
	public PasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	
}
