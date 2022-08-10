package com.example.verification.config;

//import com.example.verification.security.jwt.JWTConfigurer;
import com.example.verification.security.jwt.JwtFilter;
//import com.example.verification.security.jwt.TokenProvider;
    import lombok.AllArgsConstructor;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final JwtFilter jwtFilter;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/resource").authenticated()
        .anyRequest().permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter.class
        )
    ;
  }
//  @Autowired
//  private JwtUserDetailsService userDetailsService;
//
//  @Autowired
//  private TokenProvider tokenProvider;
//
//  @Autowired
//  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    auth
//        .userDetailsService(userDetailsService);
////        .passwordEncoder(passwordEncoder());
//  }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//        .csrf().disable()
//        // dont authenticate these requests
//        .authorizeRequests().antMatchers("/registration", "/verification", "/authentication").permitAll()
//        // other requests need to be authenticated
//        .anyRequest().authenticated()
//    .and()
//        // use stateless session
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    .and()
//        // handle authorized attempts
//        .exceptionHandling().authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//    .and()
//        // add filter to validate user cred and add token in response header
//        // .addFilter(Jwt, User)
//        .apply(securityConfigurerAdapter());
//
//  }
//
//  private JWTConfigurer securityConfigurerAdapter() {
//    return new JWTConfigurer(tokenProvider);
//  }
//
//  @Bean
//  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//    return new SecurityEvaluationContextExtension();
//  }
//
//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
}
