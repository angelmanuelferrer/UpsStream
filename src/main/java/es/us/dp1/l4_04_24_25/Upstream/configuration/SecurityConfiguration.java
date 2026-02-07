package es.us.dp1.l4_04_24_25.Upstream.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import es.us.dp1.l4_04_24_25.Upstream.configuration.jwt.AuthEntryPointJwt;
import es.us.dp1.l4_04_24_25.Upstream.configuration.jwt.AuthTokenFilter;
import es.us.dp1.l4_04_24_25.Upstream.configuration.services.UserDetailsServiceImpl;
import org.springframework.http.HttpMethod;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	private static final String ADMIN = "ADMIN";
	private static final String CLINIC_OWNER = "CLINIC_OWNER";
	

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http
			.cors(withDefaults())		
			.csrf(AbstractHttpConfigurer::disable)		
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))			
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.exceptionHandling((exepciontHandling) -> exepciontHandling.authenticationEntryPoint(unauthorizedHandler))			
			
			.authorizeHttpRequests(authorizeRequests ->	authorizeRequests
			.requestMatchers("/api/v1/developers").permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/resources/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).permitAll() 
			.requestMatchers(AntPathRequestMatcher.antMatcher("/static/**")).permitAll() 
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-resources/**")).permitAll()						
			.requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/oups")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()												
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/developers")).permitAll()												
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/plan")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/friendsInvitation/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/matchInvitation/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/friends/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/statistics/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/matches/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/profiles")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/saga")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/saga")).hasAnyAuthority(ADMIN)
			.requestMatchers( (new RegexRequestMatcher("/api/v1/saga/\\d+", "GET"))).authenticated()
			.requestMatchers( (new RegexRequestMatcher("/api/v1/saga/\\d+", null))).hasAnyAuthority(ADMIN)
			.anyRequest().authenticated())					
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);		
		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}	


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}
